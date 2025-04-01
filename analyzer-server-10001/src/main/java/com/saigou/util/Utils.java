package com.saigou.util;

import com.saigou.entity.*;
import org.bytedeco.ffmpeg.avutil.AVFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.opencv.opencv_core.Mat;

import static org.bytedeco.ffmpeg.global.avutil.av_frame_free;
import static org.bytedeco.ffmpeg.global.avutil.av_frame_unref;

public class Utils {

    public static ControlFace FaceBox2ControlFace(Long controlId,Long timestamp,FaceBox faceBox) {
        ControlFace controlFace = new ControlFace();
        controlFace.setControlId(controlId);
        controlFace.setTimestamp(timestamp);
        controlFace.setFaceId(faceBox.getFaceId());
        controlFace.setBboxMinX(faceBox.getMinPoint().getX());
        controlFace.setBboxMinY(faceBox.getMinPoint().getY());
        controlFace.setBboxMaxX(faceBox.getMaxPoint().getX());
        controlFace.setBboxMaxY(faceBox.getMaxPoint().getY());
        controlFace.setScore(faceBox.getScore());
        controlFace.setTrackId(faceBox.getTrackId());
        controlFace.setExpressionType(faceBox.getExpressionFeature());
        return controlFace;
    }

    public static FaceBox ControlFace2FaceBox(ControlFace controlFace) {
        FaceBox faceBox = new FaceBox();
        faceBox.setFaceId(controlFace.getFaceId());
        faceBox.setMinPoint(new Point(controlFace.getBboxMinX(),controlFace.getBboxMinY()));
        faceBox.setMaxPoint(new Point(controlFace.getBboxMaxX(),controlFace.getBboxMaxY()));
        faceBox.setScore(controlFace.getScore());
        faceBox.setTrackId(controlFace.getTrackId());
        faceBox.setExpressionFeature(controlFace.getExpressionType());
        return faceBox;
    }

    public static ControlPerson PersonBox2ControlPerson(Long controlId,Long timestamp,PersonBox personBox) {
        ControlPerson controlPerson = new ControlPerson();
        controlPerson.setControlId(controlId);
        controlPerson.setTimestamp(timestamp);
        controlPerson.setBboxMinX(personBox.getMinPoint().getX());
        controlPerson.setBboxMinY(personBox.getMinPoint().getY());
        controlPerson.setBboxMaxX(personBox.getMaxPoint().getX());
        controlPerson.setBboxMaxY(personBox.getMaxPoint().getY());
        controlPerson.setScore(personBox.getScore());
        controlPerson.setTrackId(personBox.getTrackId());
        controlPerson.setPostureType(personBox.getAttitudeFeature());
        return controlPerson;
    }

    public static PersonBox ControlPerson2PersonBox(ControlPerson controlPerson) {
        PersonBox personBox = new PersonBox();
        personBox.setMinPoint(new Point(controlPerson.getBboxMinX(),controlPerson.getBboxMinY()));
        personBox.setMaxPoint(new Point(controlPerson.getBboxMaxX(),controlPerson.getBboxMaxY()));
        personBox.setScore(controlPerson.getScore());
        personBox.setTrackId(controlPerson.getTrackId());
        personBox.setAttitudeFeature(controlPerson.getPostureType());
        return personBox;
    }

    public static Frame createDeepCopy(Frame frame) {
        Frame clonedFrame = new Frame();
        clonedFrame = frame.clone();
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
