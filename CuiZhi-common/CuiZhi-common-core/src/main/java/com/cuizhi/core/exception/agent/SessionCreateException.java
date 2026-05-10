package com.cuizhi.core.exception.agent;

import com.cuizhi.core.common.CzHttpStatus;

/**
 * @Author: thpaperman
 * @Date: 2026/5/5 21:27
 * @Description: 会话创建异常
 * @Version: 1.0
 */
public class SessionCreateException extends AgentException {

    public SessionCreateException(String message) {
        super(CzHttpStatus.INTERNAL_ERROR.getCode(), message);
    }

    public SessionCreateException(Integer code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
