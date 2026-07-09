package com.cuizhi.core.exception.auth;

import com.cuizhi.core.common.CzHttpStatus;

/**
 * @Author thpaperman
 * @Description 认证参数校验异常
 * @Date 2026/4/11
 * @Version 1.0
 */
public class AuthBadRequestException extends AuthException {
    public AuthBadRequestException(String message) {
        super(CzHttpStatus.BAD_REQUEST.getCode(), message);
    }

    public AuthBadRequestException(String message, Throwable cause) {
        super(CzHttpStatus.BAD_REQUEST.getCode(), message, cause);
    }
}

