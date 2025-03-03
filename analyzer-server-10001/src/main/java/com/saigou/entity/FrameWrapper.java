package com.saigou.entity;

import com.saigou.grpc.FaceBox;
import com.saigou.grpc.PersonBox;
import org.bytedeco.javacv.Frame;
import java.util.List;

public class FrameWrapper {
    public final List<FaceBox> faceBoxes;
    public final List<PersonBox> faceBoxList;
    public final Frame frame;
    public FrameWrapper(Frame frame, List<FaceBox> faceBoxes, List<PersonBox> faceBoxList) {
        this.frame = frame;
        this.faceBoxes = faceBoxes;
        this.faceBoxList = faceBoxList;
    }
}
