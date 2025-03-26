package com.saigou.util;

import com.saigou.entity.AnalysisResult;
import com.saigou.entity.FaceBox;

import java.util.List;
import java.util.stream.Collectors;

public class proto2javabean {
    public static AnalysisResult proto2javabean(com.saigou.grpc.AnalysisResult analysisResult) {
        com.saigou.entity.AnalysisResult analysisResult1 = ProtoBufUtil.copyProtoBeanToJavaBean(analysisResult, com.saigou.entity.AnalysisResult.class);
        List<FaceBox> f = analysisResult.getFaceBoxesList().stream().map(faceBox -> {
            com.saigou.entity.FaceBox faceBox1 = ProtoBufUtil.copyProtoBeanToJavaBean(faceBox, com.saigou.entity.FaceBox.class);
            return faceBox1;
        }).collect(Collectors.toList());
        analysisResult1.setFace_boxes(f);
        List<com.saigou.entity.PersonBox> p = analysisResult.getPersonBoxesList().stream().map(personBox -> {
            com.saigou.entity.PersonBox personBox1 = ProtoBufUtil.copyProtoBeanToJavaBean(personBox, com.saigou.entity.PersonBox.class);
           return personBox1;
        }).collect(Collectors.toList());
        analysisResult1.setPerson_boxes(p);
        analysisResult1.setImage_data(analysisResult.getImageData().toByteArray());
        return analysisResult1;
    }
}
