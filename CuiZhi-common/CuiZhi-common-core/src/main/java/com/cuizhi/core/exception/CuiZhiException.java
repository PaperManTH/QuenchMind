package com.cuizhi.core.exception;

import lombok.Getter;

/**
 * @Author thpaperman
 * @Description 淬知项目自定义异常
 * @Date 2026/4/11
 * @Version 1.0
 */
@Getter
public class CuiZhiException extends RuntimeException {

    /**
     * 错误码
     */
    private Integer code;
    /**
     * 错误信息
     */
    private String message;

    public CuiZhiException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public CuiZhiException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
