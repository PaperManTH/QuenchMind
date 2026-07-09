package com.cuizhi.core.exception.user;

import com.cuizhi.core.common.CzHttpStatus;

/**
 * @Author thpaperman
 * @Description 用户请求参数错误异常
 * @Date 2026/4/11
 * @Version 1.0
 */
public class UserBadRequestException extends UserException {
    public UserBadRequestException(String message) {
        super(CzHttpStatus.BAD_REQUEST.getCode(), message);
    }

    public UserBadRequestException(String message, Throwable cause) {
        super(CzHttpStatus.BAD_REQUEST.getCode(), message, cause);
    }
}

