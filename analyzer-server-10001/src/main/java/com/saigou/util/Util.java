package com.saigou.util;

import org.bytedeco.ffmpeg.avutil.AVFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.opencv.opencv_core.Mat;

import static org.bytedeco.ffmpeg.global.avutil.av_frame_free;
import static org.bytedeco.ffmpeg.global.avutil.av_frame_unref;

public class Util {
    public static Frame createDeepCopy(Frame frame) {
        Frame clonedFrame = new Frame();
        clonedFrame.image = frame.image.clone(); // 显式复制图像数据
        clonedFrame.timestamp = frame.timestamp;
        return clonedFrame;
    }

    public static void safeCloseFrame(Frame frame) {
        if (frame != null) {
            try {
                if (frame.image != null ) {
                    Object opaque = frame.opaque;
                    if (opaque != null) {
                        if (opaque instanceof AVFrame) {
                            AVFrame avFrame = (AVFrame) opaque;
                            av_frame_unref(avFrame);
                            av_frame_free(avFrame);
                        } else if (opaque instanceof Mat) {
                            Mat mat = (Mat) opaque;
                            mat.release(); // OpenCV Mat的内存释放方法
                        }
                    }
                }
            } finally {
                frame.close();
            }
        }
    }
}
