package com.saigou.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.66.0)",
    comments = "Source: video.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class VideoProcessorGrpc {

  private VideoProcessorGrpc() {}

  public static final String SERVICE_NAME = "VideoProcessor";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<VideoFrame,
          AnalysisResult> getProcessFrameMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ProcessFrame",
      requestType = VideoFrame.class,
      responseType = AnalysisResult.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<VideoFrame,
          AnalysisResult> getProcessFrameMethod() {
    io.grpc.MethodDescriptor<VideoFrame, AnalysisResult> getProcessFrameMethod;
    if ((getProcessFrameMethod = VideoProcessorGrpc.getProcessFrameMethod) == null) {
      synchronized (VideoProcessorGrpc.class) {
        if ((getProcessFrameMethod = VideoProcessorGrpc.getProcessFrameMethod) == null) {
          VideoProcessorGrpc.getProcessFrameMethod = getProcessFrameMethod =
              io.grpc.MethodDescriptor.<VideoFrame, AnalysisResult>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ProcessFrame"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  VideoFrame.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  AnalysisResult.getDefaultInstance()))
              .setSchemaDescriptor(new VideoProcessorMethodDescriptorSupplier("ProcessFrame"))
              .build();
        }
      }
    }
    return getProcessFrameMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static VideoProcessorStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<VideoProcessorStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<VideoProcessorStub>() {
        @Override
        public VideoProcessorStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new VideoProcessorStub(channel, callOptions);
        }
      };
    return VideoProcessorStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static VideoProcessorBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<VideoProcessorBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<VideoProcessorBlockingStub>() {
        @Override
        public VideoProcessorBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new VideoProcessorBlockingStub(channel, callOptions);
        }
      };
    return VideoProcessorBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static VideoProcessorFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<VideoProcessorFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<VideoProcessorFutureStub>() {
        @Override
        public VideoProcessorFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new VideoProcessorFutureStub(channel, callOptions);
        }
      };
    return VideoProcessorFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default io.grpc.stub.StreamObserver<VideoFrame> processFrame(
        io.grpc.stub.StreamObserver<AnalysisResult> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getProcessFrameMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service VideoProcessor.
   */
  public static abstract class VideoProcessorImplBase
      implements io.grpc.BindableService, AsyncService {

    @Override public final io.grpc.ServerServiceDefinition bindService() {
      return VideoProcessorGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service VideoProcessor.
   */
  public static final class VideoProcessorStub
      extends io.grpc.stub.AbstractAsyncStub<VideoProcessorStub> {
    private VideoProcessorStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected VideoProcessorStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new VideoProcessorStub(channel, callOptions);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<VideoFrame> processFrame(
        io.grpc.stub.StreamObserver<AnalysisResult> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncBidiStreamingCall(
          getChannel().newCall(getProcessFrameMethod(), getCallOptions()), responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service VideoProcessor.
   */
  public static final class VideoProcessorBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<VideoProcessorBlockingStub> {
    private VideoProcessorBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected VideoProcessorBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new VideoProcessorBlockingStub(channel, callOptions);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service VideoProcessor.
   */
  public static final class VideoProcessorFutureStub
      extends io.grpc.stub.AbstractFutureStub<VideoProcessorFutureStub> {
    private VideoProcessorFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected VideoProcessorFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new VideoProcessorFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_PROCESS_FRAME = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }

    @Override
    @SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_PROCESS_FRAME:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.processFrame(
              (io.grpc.stub.StreamObserver<AnalysisResult>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getProcessFrameMethod(),
          io.grpc.stub.ServerCalls.asyncBidiStreamingCall(
            new MethodHandlers<
                    VideoFrame,
                    AnalysisResult>(
                service, METHODID_PROCESS_FRAME)))
        .build();
  }

  private static abstract class VideoProcessorBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    VideoProcessorBaseDescriptorSupplier() {}

    @Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return VideoProto.getDescriptor();
    }

    @Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("VideoProcessor");
    }
  }

  private static final class VideoProcessorFileDescriptorSupplier
      extends VideoProcessorBaseDescriptorSupplier {
    VideoProcessorFileDescriptorSupplier() {}
  }

  private static final class VideoProcessorMethodDescriptorSupplier
      extends VideoProcessorBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    VideoProcessorMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (VideoProcessorGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new VideoProcessorFileDescriptorSupplier())
              .addMethod(getProcessFrameMethod())
              .build();
        }
      }
    }
    return result;
  }
}
