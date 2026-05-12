package com.cuizhi.core.exception.user;

import com.cuizhi.core.exception.CuiZhiException;

/**
 * 用户域通用异常基类
 */
public class UserException extends CuiZhiException {
    public UserException(Integer code, String message) {
        super(code, message);
    }

    public UserException(Integer code, String message, Throwable cause) {
        super(code, message, cause);
    }
}

