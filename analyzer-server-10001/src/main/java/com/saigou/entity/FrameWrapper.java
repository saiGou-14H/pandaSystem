package com.saigou.entity;

import com.saigou.grpc.FaceBox;
import com.saigou.grpc.PersonBox;
import lombok.Data;
import org.bytedeco.javacv.Frame;
import java.util.List;
@Data
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
