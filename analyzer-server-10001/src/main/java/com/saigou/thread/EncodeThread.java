package com.saigou.thread;

import com.google.protobuf.ByteString;
import com.saigou.entity.ImageWrapper;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Size;

import java.util.concurrent.*;

public class EncodeThread extends Thread{
    public LinkedBlockingQueue<Frame> frameQueue;
    public LinkedBlockingQueue<ImageWrapper> imageQueue;
    public CopyOnWriteArrayList<Long> keyList;
    public LinkedBlockingQueue<Frame> pushFrameQueue;
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
    public EncodeThread(LinkedBlockingQueue<Frame> frameQueue, LinkedBlockingQueue<ImageWrapper> imageQueue, LinkedBlockingQueue<Frame> pushFrameQueue, CopyOnWriteArrayList<Long> keyList) {
        this.frameQueue = frameQueue;
        this.imageQueue = imageQueue;
        this.pushFrameQueue = pushFrameQueue;
        this.keyList = keyList;
    }
    int count = 15;
    int frameCount = 0;

    private final ExecutorService frameProcessorExecutor = Executors.newFixedThreadPool(16);
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
            System.out.println("帧处理失败"+e.getMessage());
        }
    }
    @Override
    public void run() {
        while (!isInterrupted()){
            try {
                Frame frame = frameQueue.poll(5, TimeUnit.MILLISECONDS);
                if (frame != null && frame.image != null) {
                        if(frameCount%count==0){
                            frameProcessorExecutor.submit(() -> {
                                handleFrame(frame);
                            });
                        }
                    pushFrameQueue.offer(frame);
                    frameCount++;
                }
            } catch (InterruptedException e) {
                interrupt();
            }
        }
    }
    @Override
    public void interrupt() {
        super.interrupt();
        frameProcessorExecutor.shutdown(); // 温和关闭
        try {
            if (!frameProcessorExecutor.awaitTermination(2, TimeUnit.SECONDS)) {
                frameProcessorExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
