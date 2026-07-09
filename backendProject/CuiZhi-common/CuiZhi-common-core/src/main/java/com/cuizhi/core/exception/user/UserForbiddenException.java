package com.cuizhi.core.exception.user;

import com.cuizhi.core.common.CzHttpStatus;

/**
 * @Author thpaperman
 * @Description 用户权限不足异常
 * @Date 2026/4/11
 * @Version 1.0
 */
public class UserForbiddenException extends UserException {
    public UserForbiddenException(String message) {
        super(CzHttpStatus.FORBIDDEN.getCode(), message);
    }

    public UserForbiddenException(String message, Throwable cause) {
        super(CzHttpStatus.FORBIDDEN.getCode(), message, cause);
    }
}

