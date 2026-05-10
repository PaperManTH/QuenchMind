package com.cuizhi.core.exception.agent;

import com.cuizhi.core.exception.CuiZhiException;

/**
 * @Author: thpaperman
 * @Date: 2026/5/5 21:25
 * @Description: 智能体服务异常类
 * @Version: 1.0
 */
public class AgentException extends CuiZhiException {
  public AgentException(Integer code, String message) {
    super(code, message);
  }

  public AgentException(Integer code, String message, Throwable cause) {
    super(code, message, cause);
  }
}
