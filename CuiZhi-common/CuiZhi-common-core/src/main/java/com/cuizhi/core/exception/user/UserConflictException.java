package com.cuizhi.core.exception.user;

import com.cuizhi.core.common.CzHttpStatus;

public class UserConflictException extends UserException {
    public UserConflictException(String message) {
        super(CzHttpStatus.CONFLICT.getCode(), message);
    }

    public UserConflictException(String message, Throwable cause) {
        super(CzHttpStatus.CONFLICT.getCode(), message, cause);
    }
}

