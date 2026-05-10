package com.cuizhi.core.common;

import lombok.Getter;

/**
 * @Author thpaperman
 * @Description 项目统一响应 code
 * @Date 2026/4/6
 * @Version 1.0
 */
@Getter
public enum CzHttpStatus {
    SUCCESS(200, "成功"),
    FAIL(500, "失败"),
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "认证失败"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),
    CONFLICT(409, "资源冲突"),
    TOO_MANY_REQUESTS(429, "请求过于频繁"),
    BAD_GATEWAY(502, "上游服务异常"),
    INTERNAL_ERROR(500, "服务器内部错误");

    /**
     * 状态码
     */
    private final int code;
    /**
     * 状态码描述
     */
    private final String message;

    CzHttpStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
