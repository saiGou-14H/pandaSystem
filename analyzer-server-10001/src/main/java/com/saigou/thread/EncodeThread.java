package com.saigou.thread;

import com.google.protobuf.ByteString;
import com.saigou.entity.ImageWrapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

@EqualsAndHashCode(callSuper = true)
@Data
public class EncodeThread extends Thread {
    private static final Logger log = LoggerFactory.getLogger(EncodeThread.class);
    public LinkedBlockingQueue<Frame> frameQueue;
    public LinkedBlockingQueue<ImageWrapper> imageQueue;
    public CopyOnWriteArrayList<Long> keyList;
    public LinkedBlockingQueue<Frame> pushFrameQueue;
    public ThreadPoolExecutor encodingManager;
    private static final IntPointer jpegParams = new IntPointer(
            opencv_imgcodecs.IMWRITE_JPEG_QUALITY, 80//压缩率80%
    );

    public static Mat dencodeJpeg(ByteString data) {
        try (Mat inputMat = new Mat(data.toByteArray());
             Mat decodedMat = opencv_imgcodecs.
                     imdecode(inputMat, opencv_imgcodecs.IMREAD_COLOR)) {
            return decodedMat.clone(); // 返回独立副本
        }
    }

    public static ByteString encodeJpeg(Mat mat) {
        try (BytePointer buffer = new BytePointer()) {
            if (!opencv_imgcodecs.
                    imencode(".jpg", mat, buffer, jpegParams)) {
                return ByteString.EMPTY;
            }
            return ByteString.copyFrom(buffer.getStringBytes());
        }
    }

    public EncodeThread(LinkedBlockingQueue<Frame> frameQueue, LinkedBlockingQueue<ImageWrapper> imageQueue,
                        LinkedBlockingQueue<Frame> pushFrameQueue, CopyOnWriteArrayList<Long> keyList,
                        ThreadPoolExecutor encodingManager) {
        this.frameQueue = frameQueue;
        this.imageQueue = imageQueue;
        this.pushFrameQueue = pushFrameQueue;
        this.keyList = keyList;
        this.encodingManager = encodingManager;
    }

    int frameRate = 60;// 60帧进行一次算法分析
    int frameCount = 0;

    private void handleFrame(Frame frame) {
        try (OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat();
             Mat mat = converter.convert(frame);// 缩放图像
             Mat resizedMat = new Mat();
             Size newSize = new Size(mat.cols() / 2, mat.rows() / 2);) {
            opencv_imgproc.resize(mat, resizedMat, newSize);
            ByteString bytes = encodeJpeg(resizedMat);
            ImageWrapper wrapper = new ImageWrapper(bytes, frame.timestamp, mat.cols(), mat.rows());
            keyList.add(frame.timestamp);
            if (imageQueue.remainingCapacity() > 10) { // 保持缓冲余量
                imageQueue.offer(wrapper);
            } else {
                // 丢弃旧帧保持实时性
                imageQueue.poll();
                imageQueue.offer(wrapper);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("帧处理失败" + e.getMessage());
        }
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                Frame frame = frameQueue.poll(5, TimeUnit.MILLISECONDS);
                if (frame != null && frame.image != null) {
                    if (frameCount % frameRate == 0) {
                        encodingManager.submit(() -> {
                            handleFrame(frame);
                        });
                    }
                    pushFrameQueue.offer(frame);
                    frameCount++;
                }
            } catch (Exception e) {
//            log.error("编码线程异常",e);
            } finally {
            }
        }
    }

    @Override
    public void interrupt() {
        if(!isInterrupted()){
            super.interrupt();
        }
        log.info("编码线程结束");
    }
}
