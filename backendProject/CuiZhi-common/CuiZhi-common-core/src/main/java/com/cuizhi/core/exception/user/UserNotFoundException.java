package com.cuizhi.core.exception.user;

import com.cuizhi.core.common.CzHttpStatus;

/**
 * @Author thpaperman
 * @Description 用户不存在异常
 * @Date 2026/4/11
 * @Version 1.0
 */
public class UserNotFoundException extends UserException {
    public UserNotFoundException(String message) {
        super(CzHttpStatus.NOT_FOUND.getCode(), message);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(CzHttpStatus.NOT_FOUND.getCode(), message, cause);
    }
}

