package com.saigou.util;

import com.saigou.entity.AnalysisResult;
import com.saigou.entity.ControlTimestamp;
import com.saigou.entity.FaceBox;
import com.saigou.entity.PersonBox;
import lombok.Data;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
@Data
public class ClassroomAnalyzer {
    // 权重配置（可改为枚举）
    private static final Map<String, Double> EMOTION_WEIGHTS_CHINESE = Map.of(
            "开心", 0.9, "正常", 0.3, "惊讶", 0.1,"伤心", -0.5, "生气", -0.8, "厌恶", -0.7, "恐惧", -0.6
    );

    private static final Map<String, Double> EMOTION_WEIGHTS_ENGLISH = Map.of(
            "happy", 0.9, "normal", 0.3, "surprise", 0.1,
            "sad", -0.5, "angry", -0.8, "disgust", -0.7, "fear", -0.6
    );
    // EMOTION_WEIGHTS_CHINESE + EMOTION_WEIGHTS_ENGLISH
    private static final Map<String, Double> EMOTION_WEIGHTS_ALL = new HashMap<>();
    static {
        EMOTION_WEIGHTS_ALL.putAll(EMOTION_WEIGHTS_CHINESE);
        EMOTION_WEIGHTS_ALL.putAll(EMOTION_WEIGHTS_ENGLISH);
    }

    private static final Map<String, Double> POSTURE_WEIGHTS = Map.of(
            "学习", 0.8, "举手", 0.6, "睡觉", -1.0,
            "study", 0.8, "hand", 0.6, "sleep", -1.0
    );

    public static ControlTimestamp analyze(Long controlId,AnalysisResult result) {
        // 表情分析
        List<Double> emotionScores = result.getFaceBoxes().stream()
                .map(FaceBox::getExpressionFeature)
                .map(EMOTION_WEIGHTS_ALL::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // 姿态分析
        List<Double> postureScores = result.getPersonBoxes().stream()
                .map(PersonBox::getAttitudeFeature)
                .map(POSTURE_WEIGHTS::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // 计算得分
        double emotionAvg = emotionScores.isEmpty() ? 0 :
                emotionScores.stream().mapToDouble(d -> d).average().orElse(0);
        double postureAvg = postureScores.isEmpty() ? 0 :
                postureScores.stream().mapToDouble(d -> d).average().orElse(0);
        double totalScore = 0.6 * emotionAvg + 0.4 * postureAvg;
        ControlTimestamp controlTimestamp = new ControlTimestamp();
        controlTimestamp.setControlId(controlId);
        controlTimestamp.setTimestamp(result.getTimestamp());
        controlTimestamp.setTotalScore(totalScore);
        controlTimestamp.setEmotionDistribution(buildDistribution(emotionScores, EMOTION_WEIGHTS_ALL));
        controlTimestamp.setPostureDistribution(buildDistribution(postureScores, POSTURE_WEIGHTS));
        controlTimestamp.setStatus(determineStatus(totalScore));
        controlTimestamp.setAlert(checkAlerts(emotionScores, postureScores));
        // 构建报告
        return controlTimestamp;
    }

    private static Map<String, Integer> buildDistribution(List<Double> scores,
                                                          Map<String, Double> weightMap) {
        return weightMap.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> (int)scores.stream()
                                .filter(s -> s.equals(e.getValue()))
                                .count()
                ));
    }

    private static String determineStatus(double score) {
        if (score >= 0.7) return "优（高度专注）";
        if (score >= 0.3) return "良（正常学习）";
        if (score >= -0.3) return "中（轻度分心）";
        return "差（严重走神）";
    }

    private static String checkAlerts(List<Double> emotions, List<Double> postures) {
        // 姿态警报
        long sleepCount = postures.stream().filter(p -> p == -1.0).count();
        if (postures.size() > 0 && (double)sleepCount / postures.size() > 0.3) {
            return "姿态警报：超过30%学生处于睡眠状态";
        }

        // 情绪警报
        long negativeCount = emotions.stream().filter(e -> e <= -0.5).count();
        if (emotions.size() > 0 && (double)negativeCount / emotions.size() > 0.4) {
            return "情绪警报：超过40%学生呈现消极情绪";
        }

        return "正常";
    }


    // 测试用例
    public static void main(String[] args) {
        AnalysisResult input = new AnalysisResult();
        input.setTimestamp(System.currentTimeMillis());
        List<FaceBox> faceBoxes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            FaceBox faceBox = new FaceBox();
            faceBox.setExpressionFeature(EMOTION_WEIGHTS_ALL.keySet().toArray(new String[0])[i % EMOTION_WEIGHTS_ALL.size()]);
            faceBoxes.add(faceBox);
        }
        input.setFaceBoxes(faceBoxes);
        List<PersonBox> personBoxes = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            PersonBox personBox = new PersonBox();
            personBox.setAttitudeFeature(POSTURE_WEIGHTS.keySet().toArray(new String[0])[i % POSTURE_WEIGHTS.size()]);
            personBoxes.add(personBox);
        }
        input.setPersonBoxes(personBoxes);
        Long controlId = 1L;
        ControlTimestamp analyze = analyze(controlId,input);
        System.out.println(analyze);
    }
}