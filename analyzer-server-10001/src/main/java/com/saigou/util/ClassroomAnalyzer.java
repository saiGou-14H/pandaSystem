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
            "happy", 0.9, "normal", 0.3, "amaze", 0.1,
            "sad", -0.5, "angry", -0.8, "disgust", -0.7, "fear", -0.6
    );
    private static final Map<String, Double> POSTURE_WEIGHTS_CHINESE = Map.of(
            "学习", 0.8, "举手", 0.6, "睡觉", -1.0
    );
    private static final Map<String, Double> POSTURE_WEIGHTS_ENGLISH = Map.of(
            "study", 0.8, "hands", 0.6, "sleep", -1.0
    );
    // EMOTION_WEIGHTS_CHINESE + EMOTION_WEIGHTS_ENGLISH
    private static final Map<String, Double> EMOTION_WEIGHTS_ALL = new HashMap<>();
    private static final Map<String, Double> POSTURE_WEIGHTS_ALL = new HashMap<>();
    static {
        EMOTION_WEIGHTS_ALL.putAll(EMOTION_WEIGHTS_CHINESE);
        EMOTION_WEIGHTS_ALL.putAll(EMOTION_WEIGHTS_ENGLISH);
        POSTURE_WEIGHTS_ALL.putAll(POSTURE_WEIGHTS_CHINESE);
        POSTURE_WEIGHTS_ALL.putAll(POSTURE_WEIGHTS_ENGLISH);
    }

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
                .map(POSTURE_WEIGHTS_ALL::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        //中英转换
        result.getFaceBoxes().forEach(fb -> {
            if (fb.getExpressionFeature().equals("happy")) {
                fb.setExpressionFeature("开心");
            } else if (fb.getExpressionFeature().equals("normal")) {
                fb.setExpressionFeature("正常");
            } else if (fb.getExpressionFeature().equals("amaze")) {
                fb.setExpressionFeature("惊讶");
            } else if (fb.getExpressionFeature().equals("sad")) {
                fb.setExpressionFeature("伤心");
            } else if (fb.getExpressionFeature().equals("angry")) {
                fb.setExpressionFeature("生气");
            } else if (fb.getExpressionFeature().equals("disgust")) {
                fb.setExpressionFeature("厌恶");
            } else if (fb.getExpressionFeature().equals("fear")) {
                fb.setExpressionFeature("恐惧");
            }
        });
        // 计算得分
        Float emotionAvg = emotionScores.isEmpty() ? 0 :
                (float) emotionScores.stream().mapToDouble(d -> d).average().orElse(0);
        Float postureAvg = postureScores.isEmpty() ? 0 :
                (float) postureScores.stream().mapToDouble(d -> d).average().orElse(0);
        Float totalScore = (float) (0.6 * emotionAvg + 0.4 * postureAvg);
        ControlTimestamp controlTimestamp = new ControlTimestamp();
        controlTimestamp.setControlId(controlId);
        controlTimestamp.setTimestamp(result.getTimestamp());
        controlTimestamp.setTotalScore(totalScore);
        controlTimestamp.setEmotionDistribution(buildDistribution(emotionScores, EMOTION_WEIGHTS_CHINESE));
        controlTimestamp.setPostureDistribution(buildDistribution(postureScores, POSTURE_WEIGHTS_CHINESE));
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
}