package com.cuizhi.core.exception.auth;

import com.cuizhi.core.common.CzHttpStatus;

/**
 * @Author thpaperman
 * @Description 认证权限校验异常
 * @Date 2026/4/11
 * @Version 1.0
 */
public class AuthCheckException extends AuthException {

    public AuthCheckException(Integer code, String message) {
        super(code, message);
    }

    public static void fail() {
        throw new AuthCheckException(CzHttpStatus.UNAUTHORIZED.getCode(), CzHttpStatus.UNAUTHORIZED.getMessage());
    }

    public static void fail(String message) {
        throw new AuthCheckException(CzHttpStatus.UNAUTHORIZED.getCode(), message);
    }
}
