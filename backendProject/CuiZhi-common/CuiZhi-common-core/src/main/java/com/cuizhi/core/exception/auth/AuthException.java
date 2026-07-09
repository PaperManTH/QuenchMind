package com.cuizhi.core.exception.auth;

import com.cuizhi.core.exception.CuiZhiException;

/**
 * @Author thpaperman
 * @Description 用户认证异常
 * @Date 2026/4/11
 * @Version 1.0
 */
public class AuthException extends CuiZhiException {
    public AuthException(Integer code, String message) {
        super(code, message);
    }

    public AuthException(Integer code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
