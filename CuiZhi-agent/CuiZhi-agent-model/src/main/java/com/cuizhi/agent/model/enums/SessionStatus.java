package com.cuizhi.agent.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: thpaperman
 * @Date: 2026/5/4 17:05
 * @Description: 会话状态枚举
 * @Version: 1.0
 */
@Getter
@AllArgsConstructor
public enum SessionStatus {

    NORMAL(1, "正常"),
    CLOSED(0, "删除"),
    ARCHIVED(2, "归档");

    private final Integer code;
    private final String description;

    /**
     * 根据 code 获取枚举
     */
    public static SessionStatus of(Integer code) {
        for (SessionStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("无效的会话状态: " + code);
    }
}