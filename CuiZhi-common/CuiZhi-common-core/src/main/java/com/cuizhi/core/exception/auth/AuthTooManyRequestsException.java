package com.cuizhi.core.exception.auth;

import com.cuizhi.core.common.CzHttpStatus;

public class AuthTooManyRequestsException extends AuthException {
    public AuthTooManyRequestsException(String message) {
        super(CzHttpStatus.TOO_MANY_REQUESTS.getCode(), message);
    }

    public AuthTooManyRequestsException(String message, Throwable cause) {
        super(CzHttpStatus.TOO_MANY_REQUESTS.getCode(), message, cause);
    }
}

