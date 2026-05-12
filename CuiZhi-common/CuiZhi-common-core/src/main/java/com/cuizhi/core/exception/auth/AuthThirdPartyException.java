package com.cuizhi.core.exception.auth;

import com.cuizhi.core.common.CzHttpStatus;

/**
 * 认证域内的第三方服务异常（微信/邮箱服务等）
 */
public class AuthThirdPartyException extends AuthException {
    public AuthThirdPartyException(String message) {
        super(CzHttpStatus.BAD_GATEWAY.getCode(), message);
    }

    public AuthThirdPartyException(String message, Throwable cause) {
        super(CzHttpStatus.BAD_GATEWAY.getCode(), message, cause);
    }
}

