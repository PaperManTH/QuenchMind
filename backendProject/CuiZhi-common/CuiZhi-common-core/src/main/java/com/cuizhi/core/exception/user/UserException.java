package com.cuizhi.core.exception.user;

import com.cuizhi.core.exception.CuiZhiException;

/**
 * @Author thpaperman
 * @Description 用户域通用异常基类
 * @Date 2026/4/11
 * @Version 1.0
 */
public class UserException extends CuiZhiException {
    public UserException(Integer code, String message) {
        super(code, message);
    }

    public UserException(Integer code, String message, Throwable cause) {
        super(code, message, cause);
    }
}

