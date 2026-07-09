package com.cuizhi.learning.client;

import com.cuizhi.core.exception.learning.StudyPlanGenerateException;
import com.cuizhi.learning.config.AiEngineProperties;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * @Author thpaperman
 * @Description Python AI 引擎客户端（learning 模块专用）
 * @Date 2026/7/7
 * @Version 1.0
 */
@Slf4j
@Component
public class LearningAiClient {

    private final AiEngineProperties props;
    private WebClient webClient;

    public LearningAiClient(AiEngineProperties props) {
        this.props = props;
    }

    @PostConstruct
    void init() {
        this.webClient = WebClient.builder().baseUrl(props.getBaseUrl()).build();
    }

    /**
     * AI 生成学习计划
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> generateStudyPlan(String goal, int days, int dailyMinutes) {
        try {
            Map<String, Object> resp = webClient.post()
                    .uri("/api/v1/study-plan/generate")
                    .bodyValue(Map.of(
                            "goal", goal,
                            "days", days,
                            "daily_minutes", dailyMinutes
                    ))
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block(Duration.ofMillis(props.getReadTimeout()));
            return resp != null ? (List<Map<String, Object>>) resp.get("tasks") : List.of();
        } catch (Exception e) {
            log.error("AI 生成学习计划失败: {}", e.getMessage());
            throw new StudyPlanGenerateException("AI 生成学习计划失败", e);
        }
    }
}
