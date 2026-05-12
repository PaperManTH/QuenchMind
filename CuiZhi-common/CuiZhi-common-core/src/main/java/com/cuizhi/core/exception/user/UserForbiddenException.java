package com.cuizhi.core.exception.user;

import com.cuizhi.core.common.CzHttpStatus;

public class UserForbiddenException extends UserException {
    public UserForbiddenException(String message) {
        super(CzHttpStatus.FORBIDDEN.getCode(), message);
    }

    public UserForbiddenException(String message, Throwable cause) {
        super(CzHttpStatus.FORBIDDEN.getCode(), message, cause);
    }
}

