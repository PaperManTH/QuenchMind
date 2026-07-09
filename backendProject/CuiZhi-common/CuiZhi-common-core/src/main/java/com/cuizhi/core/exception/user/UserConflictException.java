package com.cuizhi.core.exception.user;

import com.cuizhi.core.common.CzHttpStatus;

/**
 * @Author thpaperman
 * @Description 用户资源冲突异常
 * @Date 2026/4/11
 * @Version 1.0
 */
public class UserConflictException extends UserException {
    public UserConflictException(String message) {
        super(CzHttpStatus.CONFLICT.getCode(), message);
    }

    public UserConflictException(String message, Throwable cause) {
        super(CzHttpStatus.CONFLICT.getCode(), message, cause);
    }
}

