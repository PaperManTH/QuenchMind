package com.cuizhi.core.exception.auth;

import com.cuizhi.core.common.CzHttpStatus;

public class AuthBadRequestException extends AuthException {
    public AuthBadRequestException(String message) {
        super(CzHttpStatus.BAD_REQUEST.getCode(), message);
    }

    public AuthBadRequestException(String message, Throwable cause) {
        super(CzHttpStatus.BAD_REQUEST.getCode(), message, cause);
    }
}

