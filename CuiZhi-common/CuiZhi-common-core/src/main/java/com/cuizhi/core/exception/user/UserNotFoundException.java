package com.cuizhi.core.exception.user;

import com.cuizhi.core.common.CzHttpStatus;

public class UserNotFoundException extends UserException {
    public UserNotFoundException(String message) {
        super(CzHttpStatus.NOT_FOUND.getCode(), message);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(CzHttpStatus.NOT_FOUND.getCode(), message, cause);
    }
}

