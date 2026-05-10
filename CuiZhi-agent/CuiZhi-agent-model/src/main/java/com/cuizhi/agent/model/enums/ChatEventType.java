package com.cuizhi.agent.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: thpaperman
 * @Date: 2026/5/6 22:06
 * @Description: 对话事件类型
 * @Version: 1.0
 */
@Getter
@AllArgsConstructor
public enum ChatEventType {

    CHAT_MESSAGE(10011, "对话消息"),

    CHAT_END(10012, "结束对话"),

    CHAT_ERROR(10404, "对话错误"),

    CHAT_RETRIEVAL(10020, "对话检索");

    private final Integer eventCode;
    private final String desc;

    public static ChatEventType getByCode(Integer code) {
        for (ChatEventType value : values()) {
            if (value.eventCode.equals(code)) {
                return value;
            }
        }
        throw new RuntimeException("不存在该事件类型");
    }
}
