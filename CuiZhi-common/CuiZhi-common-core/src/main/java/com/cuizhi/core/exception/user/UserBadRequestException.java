package com.cuizhi.core.exception.user;

import com.cuizhi.core.common.CzHttpStatus;

public class UserBadRequestException extends UserException {
    public UserBadRequestException(String message) {
        super(CzHttpStatus.BAD_REQUEST.getCode(), message);
    }

    public UserBadRequestException(String message, Throwable cause) {
        super(CzHttpStatus.BAD_REQUEST.getCode(), message, cause);
    }
}

