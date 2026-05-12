package com.cuizhi.core.exception.auth;

import com.cuizhi.core.common.CzHttpStatus;

public class AuthConflictException extends AuthException {
    public AuthConflictException(String message) {
        super(CzHttpStatus.CONFLICT.getCode(), message);
    }

    public AuthConflictException(String message, Throwable cause) {
        super(CzHttpStatus.CONFLICT.getCode(), message, cause);
    }
}

