package com.saigou.thread;

import com.google.protobuf.ByteString;
import com.saigou.api.service.IRedisAnalyzerResultService;
import com.saigou.draw.Draw;
import com.saigou.entity.FrameWrapper;
import com.saigou.entity.ImageWrapper;
import com.saigou.grpc.*;
import com.saigou.properties.AnalyzerProperties;
import com.saigou.util.ProtoBufUtil;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
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
    public OpenCVFrameConverter.ToMat converter;
    private AnalyzerProperties analyzerProperties;
    private IRedisAnalyzerResultService iRedisAnalyzerResultService;
    ThreadPoolExecutor dencodingManager;
    private Long controlId;
    public boolean isAlive = false;

    public AnalyzerThread(LinkedBlockingQueue<ImageWrapper> imageQueue,
                          ConcurrentSkipListMap<Long, FrameWrapper> resultCache,
                          AnalyzerProperties analyzerProperties, IRedisAnalyzerResultService iRedisAnalyzerResultService
            , Long controlId, ThreadPoolExecutor dencodingManager) {
        this.resultCache = resultCache;
        this.imageQueue = imageQueue;
        converter = new OpenCVFrameConverter.ToMat();
        this.analyzerProperties = analyzerProperties;
        this.iRedisAnalyzerResultService = iRedisAnalyzerResultService;
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
    }

    @Override
    public void run() {
        isAlive = true;
        try {
            while (!isInterrupted()) {
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

            }
        } catch (Exception e) {
//            log.error("算法解析错误", e);
        } finally {
            isAlive = false;
        }
    }

    @Override
    public void onNext(AnalysisResult analysisResult) {
        ByteString imageData = analysisResult.getImageData();
        if (imageData != null) {
            try {
                // jpeg解码
                dencodingManager.submit(() -> {
                    iRedisAnalyzerResultService.addAnalysisResult2Hash(controlId, analysisResult.getTimestamp(), ProtoBufUtil.copyProtoBeanToJavaBean(analysisResult,
                            com.saigou.entity.AnalysisResult.class));

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
        if (converter != null) {
            converter.close();
        }
    }
}
