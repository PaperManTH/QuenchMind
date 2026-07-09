package com.cuizhi.core.exception.auth;

import com.cuizhi.core.common.CzHttpStatus;

/**
 * @Author thpaperman
 * @Description 认证权限请求过多异常
 * @Date 2026/4/11
 * @Version 1.0
 */
public class AuthTooManyRequestsException extends AuthException {
    public AuthTooManyRequestsException(String message) {
        super(CzHttpStatus.TOO_MANY_REQUESTS.getCode(), message);
    }

    public AuthTooManyRequestsException(String message, Throwable cause) {
        super(CzHttpStatus.TOO_MANY_REQUESTS.getCode(), message, cause);
    }
}

