package com.saigou.thread;

import cn.hutool.json.JSONUtil;
import com.google.protobuf.ByteString;
import com.saigou.api.service.IRedisAnalyzerResultService;
import com.saigou.draw.Draw;
import com.saigou.entity.FrameWrapper;
import com.saigou.entity.ImageWrapper;
import com.saigou.entity.KafkaEntity;
import com.saigou.grpc.*;
import com.saigou.properties.AnalyzerProperties;
import com.saigou.util.ClassroomAnalyzer;
import com.saigou.util.KafkaSendService;
import com.saigou.util.ProtoBufUtil;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.opencv_core.Mat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.*;

import static com.saigou.thread.EncodeThread.dencodeJpeg;

public class AnalyzerThread extends Thread implements StreamObserver<AnalysisResult> {
    private static final Logger log = LoggerFactory.getLogger(AnalyzerThread.class);
    public LinkedBlockingQueue<ImageWrapper> imageQueue;
    public ConcurrentSkipListMap<Long, FrameWrapper> resultCache;
    public ManagedChannel channel;
    public VideoProcessorGrpc.VideoProcessorStub stub;
    public StreamObserver<VideoFrame> requestObserver;
    final AnalyzerProperties analyzerProperties;
    final IRedisAnalyzerResultService iRedisAnalyzerResultService;
    final KafkaSendService kafkaSendService;
    ThreadPoolExecutor dencodingManager;
    final Long controlId;

    public AnalyzerThread(LinkedBlockingQueue<ImageWrapper> imageQueue,
                          ConcurrentSkipListMap<Long, FrameWrapper> resultCache,
                          AnalyzerProperties analyzerProperties, IRedisAnalyzerResultService iRedisAnalyzerResultService,
                          KafkaSendService kafkaSendService, Long controlId, ThreadPoolExecutor dencodingManager) {
        this.resultCache = resultCache;
        this.imageQueue = imageQueue;
        this.analyzerProperties = analyzerProperties;
        this.iRedisAnalyzerResultService = iRedisAnalyzerResultService;
        this.kafkaSendService = kafkaSendService;
        this.controlId = controlId;
        this.dencodingManager = dencodingManager;
        init();
    }

    public void init() {
        channel = ManagedChannelBuilder.forAddress(analyzerProperties.getServer().get(0).getHost(),
                        analyzerProperties.getServer().get(0).getPort())
                .usePlaintext()
                .maxInboundMessageSize(analyzerProperties.getMaxInboundMessageSize())
                .enableRetry().maxRetryAttempts(5).build();
        stub = VideoProcessorGrpc.newStub(channel);
        requestObserver = stub.processFrame(this);
        log.info("算法服务启动成功");
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                ImageWrapper wrapper;
                wrapper = imageQueue.poll(5, TimeUnit.MILLISECONDS);
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
            } catch (Exception e) {
//                log.error("算法解析错误", e);
            }
        }
    }

    @Override
    public void onNext(AnalysisResult analysisResult) {
        ByteString imageData = analysisResult.getImageData();
        if (imageData != null) {
            try {
                // jpeg解码
                dencodingManager.submit(() -> {
                    com.saigou.entity.AnalysisResult javaBeanResult = ProtoBufUtil.copyProtoBeanToJavaBean(analysisResult,
                            com.saigou.entity.AnalysisResult.class);
                    javaBeanResult.setControlTimestamp(ClassroomAnalyzer.analyze(controlId,javaBeanResult));
                    iRedisAnalyzerResultService.addAnalysisResult2Hash(controlId, analysisResult.getTimestamp(), javaBeanResult);
                    kafkaSendService.send(new KafkaEntity(controlId, javaBeanResult));
                    Mat mat = dencodeJpeg(imageData);//释放该资源会导致帧顺序错误
                    List<FaceBox> faceBoxes = null;
                    List<PersonBox> expressions = null;
                    if (!analysisResult.getFaceBoxesList().isEmpty()) {
                        faceBoxes = analysisResult.getFaceBoxesList();
                        for (FaceBox faceBox : faceBoxes) {
                            Draw.drawRectangle(mat, faceBox.getMinPoint(), faceBox.getMaxPoint());
                            Draw.drawText(mat, faceBox.getExpressionFeature(), faceBox.getMinPoint());
                        }
                    }
                    if (!analysisResult.getPersonBoxesList().isEmpty()) {
                        expressions = analysisResult.getPersonBoxesList();
                        for (PersonBox personBox : expressions) {
                            List<Point> points = personBox.getPointsList();
                            Draw.drawPersonPose(mat, points);
                        }
                    }
                    try(OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat()){
                        Frame frame = converter.convert(mat);
                        frame.timestamp = analysisResult.getTimestamp();
                        FrameWrapper frameWrapper = new FrameWrapper(frame, faceBoxes, expressions);
                        resultCache.put(frame.timestamp, frameWrapper);
                    }
                });
            } catch (Exception e) {
                log.error("处理图像线程时出错: " + e);
            }
        }
    }


    @Override
    public void onError(Throwable throwable) {
        log.warn("处理图像线程: " + throwable);
    }

    @Override
    public void onCompleted() {
        log.info("处理图像线程结束");
    }
    public void shutdown() {
        // 1. 关闭请求流（如果是流式调用）
        if (requestObserver != null) {
            requestObserver.onCompleted(); // 通知服务器流结束
        }

        // 2. 关闭 Channel
        if (channel != null && !channel.isShutdown()) {
            channel.shutdown(); // 启动优雅关闭
            try {
                // 等待 1 秒让未完成请求处理
                if (!channel.awaitTermination(500, TimeUnit.MILLISECONDS)) {
                    channel.shutdownNow(); // 强制关闭
                    channel.awaitTermination(500, TimeUnit.MILLISECONDS); // 再次等待
                }
            } catch (InterruptedException e) {
                channel.shutdownNow(); // 强制关闭
            }
        }
    }


    @Override
    public void interrupt() {
        if(!isInterrupted()){
            super.interrupt();
        }
        shutdown();
        log.info("分析线程结束");
    }
}
