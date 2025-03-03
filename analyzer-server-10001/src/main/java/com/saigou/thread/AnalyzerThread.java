package com.saigou.thread;

import com.google.protobuf.ByteString;
import com.saigou.draw.Draw;
import com.saigou.entity.FrameWrapper;
import com.saigou.entity.ImageWrapper;
import com.saigou.grpc.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.opencv_core.Mat;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static com.saigou.thread.EncodeThread.dencodeJpeg;
public class AnalyzerThread extends Thread implements StreamObserver<AnalysisResult> {
    public LinkedBlockingQueue<ImageWrapper> imageQueue;
    public ConcurrentSkipListMap<Long, FrameWrapper> resultCache;
    public final ManagedChannel channel;
    public final VideoProcessorGrpc.VideoProcessorStub stub;
    public StreamObserver<VideoFrame> requestObserver;
    private final ExecutorService frameProcessorExecutor = Executors.newFixedThreadPool(16);
    public OpenCVFrameConverter.ToMat converter;
    public AnalyzerThread(LinkedBlockingQueue<ImageWrapper> imageQueue, ConcurrentSkipListMap<Long,FrameWrapper> resultCache){
        this.resultCache = resultCache;
        this.imageQueue = imageQueue;
        channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .maxInboundMessageSize(100 * 1024 * 1024) // 100MB
                .build();
        stub = VideoProcessorGrpc.newStub(channel);
        requestObserver = stub.processFrame(this);
        converter = new OpenCVFrameConverter.ToMat();
    }
    @Override
    public void run() {
        while (!isInterrupted()){
            try{
                ImageWrapper wrapper = imageQueue.poll(5, TimeUnit.MILLISECONDS);
                Algorithm face = Algorithm.newBuilder().setName("face").setType(1).build();

                Algorithm pose = Algorithm.newBuilder().setName("pose").setType(2).build();
                if (wrapper != null && wrapper.imageData != null) {
                    VideoFrame videoFrame = VideoFrame.newBuilder()
                            .setImageData(wrapper.imageData)
                            .setTimestamp(wrapper.timestamp)
                            .setHeight(wrapper.imageHeight)
                            .setWidth(wrapper.imageWidth)
                            .addAlgorithms(face)
                            .addAlgorithms(pose)
                            .build();
                    requestObserver.onNext(videoFrame);
                }
            }catch (InterruptedException e){
                interrupt();
            }
        }
    }

    @Override
    public void onNext(AnalysisResult analysisResult) {
        ByteString imageData = analysisResult.getImageData();
        if (imageData != null) {
            try {
                // jpeg解码
                frameProcessorExecutor.submit(() -> {
                    Mat mat = dencodeJpeg(imageData);//释放该资源会导致帧顺序错误
                    List<FaceBox> faceBoxes = null;
                    List<PersonBox> expressions = null;
                    if(!analysisResult.getFaceBoxesList().isEmpty()){
                        faceBoxes = analysisResult.getFaceBoxesList();
                        for (FaceBox faceBox : faceBoxes) {
                            Draw.drawRectangle(mat, faceBox.getMinPoint(), faceBox.getMaxPoint());
                            Draw.drawText(mat, faceBox.getExpressionFeature(), faceBox.getMinPoint());
                        }
                    }
                    if(!analysisResult.getPersonBoxesList().isEmpty()){
                        expressions = analysisResult.getPersonBoxesList();
                        for (PersonBox personBox : expressions) {
                            List<Point> points = personBox.getPointsList();
                            Draw.drawPersonPose(mat, points);
                        }
                    }
                    Frame frame = converter.convert(mat);
                    frame.timestamp = analysisResult.getTimestamp();
                    FrameWrapper frameWrapper = new FrameWrapper(frame, faceBoxes, expressions);
                    resultCache.put(frame.timestamp, frameWrapper);
                });
            } catch (Exception e) {
                System.out.println("处理图像时出错: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }


    @Override
    public void onError(Throwable throwable) {
        System.err.println("gRPC 流错误: " + throwable.getMessage());
        // 尝试重建连接
        try {
            requestObserver = stub.processFrame(this); // 重连
        } catch (Exception e) {
            System.err.println("重连失败: " + e.getMessage());
        }
    }

    @Override
    public void onCompleted() {
        System.out.println("处理图像完成");
    }

    @Override
    public void interrupt() {
        super.interrupt();
        channel.shutdown();
        frameProcessorExecutor.shutdown();
        if (converter!=null){
            converter.close();
        }
    }
}
