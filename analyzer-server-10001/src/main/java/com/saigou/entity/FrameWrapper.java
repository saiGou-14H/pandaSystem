package com.saigou.entity;

import com.saigou.grpc.FaceBox;
import com.saigou.grpc.PersonBox;
import org.bytedeco.javacv.Frame;
import java.util.List;

public class FrameWrapper {
    public final List<FaceBox> faceBoxes;
    public final List<PersonBox> PersonBoxs;
    public final Frame frame;
    public FrameWrapper(Frame frame, List<FaceBox> faceBoxes, List<PersonBox> PersonBoxs) {
        this.frame = frame;
        this.faceBoxes = faceBoxes;
        this.PersonBoxs = PersonBoxs;
    }
}
