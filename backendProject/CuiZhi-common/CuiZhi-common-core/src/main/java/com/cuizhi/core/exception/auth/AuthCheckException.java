package com.cuizhi.core.exception.auth;

import com.cuizhi.core.common.CzHttpStatus;

/**
 * @Author thpaperman
 * @Description 认证权限校验异常
 * @Date 2026/4/11
 * @Version 1.0
 */
public class AuthCheckException extends AuthException {

    public AuthCheckException(String message) {
        super(CzHttpStatus.UNAUTHORIZED.getCode(), message);
    }

    public AuthCheckException(String message, Throwable cause) {
        super(CzHttpStatus.UNAUTHORIZED.getCode(), message, cause);
    }
}
