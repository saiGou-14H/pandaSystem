//package com.saigou;
//
//import com.google.protobuf.ByteString;
//import com.saigou.grpc.AnalysisResult;
//import com.saigou.grpc.BoundingBox;
//import com.saigou.grpc.VideoFrame;
//import com.saigou.grpc.VideoProcessorGrpc;
//import io.grpc.ManagedChannel;
//import io.grpc.ManagedChannelBuilder;
//import io.grpc.stub.StreamObserver;
//import lombok.SneakyThrows;
//import org.bytedeco.ffmpeg.global.avcodec;
//import org.bytedeco.ffmpeg.global.avutil;
//import org.bytedeco.javacv.*;
//import org.bytedeco.javacv.Frame;
//import org.bytedeco.opencv.global.opencv_core;
//import org.bytedeco.opencv.opencv_core.Mat;
//
//import java.awt.*;
//import java.awt.image.BufferedImage;
//import java.io.ByteArrayOutputStream;
//import java.util.Date;
//import java.util.Queue;
//import java.util.concurrent.LinkedBlockingQueue;
//
//class PullThread extends Thread{
//    public String url;
//    public FFmpegFrameGrabber grabber;
//    public Queue<Frame> frameQueue;
//
//    @SneakyThrows
//    public PullThread(String url, Queue<Frame> frameQueue) {
//        this.url = url;
//        this.frameQueue = frameQueue;
//
//        this.grabber = new FFmpegFrameGrabber(this.url);
//        //关键配置：启用AMF硬件解码
//        grabber.setOption("hwaccel", "amf");         // 指定使用AMF加速
//        grabber.setOption("hwaccel_device", "gpu");  // 指定GPU设备
//        grabber.setOption("rtsp_transport", "tcp");
//        grabber.start();
//    }
//    @SneakyThrows
//    public void run(){
//        Frame frame;
//        while (!Thread.currentThread().isInterrupted() && ((frame = grabber.grab()) != null)) {
//            if (frame.image != null){
//                frameQueue.offer(frame.clone());
//            }
//        }
//    }
//    @Override
//    public void interrupt(){
//        super.interrupt();
//        if (grabber != null) {
//            try {
//                grabber.stop();
//                grabber.release();
//                grabber.close();
//            } catch (FrameGrabber.Exception e) {
//                throw new RuntimeException(e);
//            }
//        }
//        System.out.println("拉流结束");
//    }
//}
//class PushThread extends Thread{
//    public FFmpegFrameGrabber grabber;
//    public FFmpegFrameRecorder recorder;
//    public Queue<Frame> frameQueue;
//    public final int maxImageWidth = 1920;
//    public final int maxImageHeight = 1080;
//    @SneakyThrows
//    public PushThread(String url, FFmpegFrameGrabber grabber,Queue frameQueue) {
//        this.grabber = grabber;
//        this.frameQueue = frameQueue;
//        // 2. 推流初始化
//        recorder = new FFmpegFrameRecorder(
//                url,
//                maxImageWidth,
//                maxImageHeight
//        );
//
//        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
//        recorder.setFrameRate(grabber.getFrameRate());
//        recorder.setVideoBitrate(grabber.getVideoBitrate()); // 码率与输入一致 (10_000_000 10Mbs)
//        recorder.setFormat("flv");
//        recorder.setVideoOption("tune", "zerolatency"); // 零延迟模式
//        recorder.setVideoOption("crf", "23"); // 画质与码率平衡
//        recorder.setGopSize(60); // 关键帧间隔（帧数）
//        // AMF 硬件编码专用参数
//        recorder.setVideoOption("preset", "ultrafast");     // 预设模式
//        recorder.setVideoOption("quality", "speed");        // 速度优先
//        recorder.setVideoCodecName("h264_amf");// AMD
////        recorder.setVideoCodecName("h264_nvenc"); // NVIDIA
//        recorder.setVideoOption("rc", "cbr_ld_hq");         // 低延迟码率控制
//        recorder.setVideoOption("usage", "ultralowlatency");// 超低延迟模式
//        // 限制线程数（AMF对多线程支持有限）
//        recorder.setVideoOption("threads", "8");
//        recorder.setPixelFormat(avutil.AV_PIX_FMT_YUV420P);
//        recorder.setVideoOption("pix_fmt", "yuv420p");      // 双重保险
//        recorder.start();
//    }
//
//    @SneakyThrows
//    public void run(){
//        VideoStreamClient videoStreamClient = new VideoStreamClient("localhost", 50051);
//        StreamObserver<VideoFrame> requestObserver = videoStreamClient.requestObserver;
//
//
//        // 在程序初始化时启用 OpenCL
//        opencv_core.setUseOpenCL(true);
//        // 验证加速是否生效
//        System.out.println("OpenCL 启用状态: " + opencv_core.useOpenCL());
//
//        Java2DFrameConverter javaConverter = new Java2DFrameConverter();
//        OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat();
//        Graphics2D g2d;
//        ByteArrayOutputStream baos = null;
//        Mat mat;
//        Frame frame;
//        BufferedImage image;
//
//        long startTime = System.currentTimeMillis();
//        int frameCount = 0;
//        while (!Thread.currentThread().isInterrupted()) {
//            if (frameQueue.isEmpty()) {
//                continue;
//            }
//            frame = frameQueue.poll();
//            if (frame != null) {
//                Long start = new Date().getTime();
//
////                // 处理帧（添加水印)
////                image = javaConverter.getBufferedImage(frame);
////                g2d = image.createGraphics();
////                g2d.setColor(Color.red);
////                g2d.setFont(new Font("Arial", Font.BOLD, 100));
////                g2d.drawString("Live", grabber.getImageWidth()/2, grabber.getImageHeight()/2);
////                g2d.dispose();
////                baos = new ByteArrayOutputStream();
////                ImageIO.write(image, "jpg", baos);
//                mat = converter.convertToMat(frame);
//                byte[] data = new byte[(int) mat.arraySize()];
//                mat.data().get(data);
////
//                com.saigou.grpc.VideoFrame videoFrame = com.saigou.grpc.VideoFrame.newBuilder()
//                        .setJpegData(ByteString.copyFrom(data))
//                        .setTimestamp(new Date().getTime())
//                        .setAlgorithmsType(0)
//                        .setHeight(grabber.getImageHeight())
//                        .setWidth(grabber.getImageWidth())
//                        .build();
//                requestObserver.onNext(videoFrame);
//
//                recorder.record(frame);
//
//                frameCount++;
//                Long cost = new Date().getTime() - start;
//                System.out.println("处理帧耗时：" + cost+"ms");
//
//                // 每 5 秒输出一次帧率
//                if (System.currentTimeMillis() - startTime > 5000) {
//                    double fps = frameCount / 5.0;
//                    System.out.printf("实际推流帧率: %.2f FPS\n", fps);
//                    frameCount = 0;
//                    startTime = System.currentTimeMillis();
//                }
//            }
//        }
//    }
//    @Override
//    public void interrupt(){
//        super.interrupt();
//        if (recorder != null) {
//            try {
//                recorder.stop();    // 1. 停止录制并写入尾部
//                recorder.release(); // 2. 释放本地内存
//                recorder.close();   // 3. 关闭输出流
//            } catch (FrameRecorder.Exception e) {
//                throw new RuntimeException(e);
//            }
//        }
//        System.out.println("推流结束");
//    }
//}
//class videoStreamProcessor {
//
//    private Queue<Frame> frameQueue = new LinkedBlockingQueue<>(100);
//    PullThread pullThread;
//    PushThread pushThread;
//    videoStreamProcessor(String pullUrl, String pushUrl) {
//        this.pullThread = new PullThread(pullUrl,frameQueue);
//        this.pushThread = new PushThread(pushUrl, pullThread.grabber,frameQueue);
//    }
//    public void start() {
//        pullThread.start();
//        pushThread.start();
//    }
//
//    public void stop() {
//        pushThread.interrupt();
//        pullThread.interrupt();
//        frameQueue.clear();
//    }
//
//}
//class VideoStreamClient{
//    public final ManagedChannel channel;
//    public final VideoProcessorGrpc.VideoProcessorStub stub;
//
//    public AnalysisResultHandler responseObserver;
//    public StreamObserver<VideoFrame> requestObserver;
//
//    public VideoStreamClient(String host, int port) {
//        channel = ManagedChannelBuilder.forAddress(host, port)
//                .usePlaintext()
//                .maxInboundMessageSize(100 * 1024 * 1024) // 100MB
//                .build();
//        stub = VideoProcessorGrpc.newStub(channel);
//        responseObserver = new AnalysisResultHandler();
//        requestObserver = stub.processFrame(responseObserver);
//    }
//
//
//
//    class AnalysisResultHandler implements StreamObserver<AnalysisResult> {
//        public String aa;
//        @Override
//        public void onNext(AnalysisResult result) {
//            System.out.printf("[%dms] %d bounding boxes\n",
//                    result.getTimestamp(),
//                    result.getBoxesCount());
//            for (BoundingBox box : result.getBoxesList()) {
//                System.out.printf("[%s][%dms] %s %.2f%%\n",
//                        box.getLabel(),
//                        result.getTimestamp(),
//                        box.getLabel(),
//                        box.getScore() * 100);
//
//            }
//        }
//
//        @Override
//        public void onError(Throwable t) {
//            System.err.println("分析服务错误: " + t.getMessage());
//        }
//
//        @Override
//        public void onCompleted() {
//            System.out.println("分析服务连接关闭");
//        }
//
//
//    }
//}
//public class Processor {
//    static {
//        // 启用FFmpeg详细日志
//        FFmpegLogCallback.set();
//    }
//
//    public static void main(String[] args) throws InterruptedException {
//        String url1 = "rtsp://127.0.0.1:7554/live/test";
//        String url2 = "rtmp://127.0.0.1:7935/live/test3";
//        videoStreamProcessor videoStreamProcessor = new videoStreamProcessor(url1, url2);
//        videoStreamProcessor.start();
//        Thread.sleep(1000000);
//        videoStreamProcessor.stop();
//    }
//}
