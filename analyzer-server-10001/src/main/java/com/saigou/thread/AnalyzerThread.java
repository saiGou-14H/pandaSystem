package com.saigou.thread;

import com.google.protobuf.ByteString;
import com.saigou.entity.ImageWrapper;
import com.saigou.grpc.AnalysisResult;
import com.saigou.grpc.VideoFrame;
import com.saigou.grpc.VideoProcessorGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameConverter;

import java.util.concurrent.*;

import static com.saigou.thread.EncodeThread.dencodeJpeg;

public class AnalyzerThread extends Thread implements StreamObserver<AnalysisResult> {
    public LinkedBlockingQueue<ImageWrapper> imageQueue;
    public ConcurrentSkipListMap resultCache;
    public final ManagedChannel channel;
    public final VideoProcessorGrpc.VideoProcessorStub stub;
    public StreamObserver<VideoFrame> requestObserver;
    public OpenCVFrameConverter.ToMat converter;
    private final ExecutorService frameProcessorExecutor = Executors.newFixedThreadPool(16);
    public AnalyzerThread(LinkedBlockingQueue<ImageWrapper> imageQueue, ConcurrentSkipListMap<Long,Frame> resultCache) {
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
                if (wrapper != null && wrapper.imageData != null) {
                    VideoFrame videoFrame = VideoFrame.newBuilder()
                            .setImageData(wrapper.imageData)
                            .setTimestamp(wrapper.timestamp)
                            .setAlgorithmsType(0)
                            .setHeight(1080)
                            .setWidth(1920)
                            .build();
                    requestObserver.onNext(videoFrame);
                }
            }catch (InterruptedException e){
                e.printStackTrace();
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
                    Frame frame = converter.convert(dencodeJpeg(imageData));
                    frame.timestamp = analysisResult.getTimestamp();
                    resultCache.put(frame.timestamp, frame);
                });
            } catch (Exception e) {
                System.out.println("处理图像时出错: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }


    @Override
    public void onError(Throwable throwable) {
        System.out.println("处理图像时出错: " + throwable.getMessage());
    }

    @Override
    public void onCompleted() {
        System.out.println("处理图像完成");
    }

    @Override
    public void interrupt() {
        super.interrupt();
        requestObserver.onCompleted();
        channel.shutdown();
        frameProcessorExecutor.shutdown();
    }
}
