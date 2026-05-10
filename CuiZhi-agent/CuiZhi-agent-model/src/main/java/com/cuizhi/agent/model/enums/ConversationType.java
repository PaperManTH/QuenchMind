package com.cuizhi.agent.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: thpaperman
 * @Date: 2026/5/6 21:24
 * @Description: 会话类型
 * @Version: 1.0
 */
@Getter
@AllArgsConstructor
public enum ConversationType {

    CHAT("CHAT", "聊天"),
    LEARNING_QA("LEARNING_QA", "学习问答"),
    TASK_GENERATION("TASK_GENERATION", "任务生成"),
    WRONG_QUESTION("WRONG_QUESTION", "错题分析");

    private final String type;
    private final String description;

    public static ConversationType of(String type) {
        for (ConversationType value : values()) {
            if (value.getType().equals(type)) {
                return value;
            }
        }
        throw new IllegalArgumentException("无效的会话类型: " + type);
    }
}
