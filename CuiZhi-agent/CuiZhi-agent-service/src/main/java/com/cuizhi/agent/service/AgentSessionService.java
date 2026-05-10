package com.cuizhi.agent.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cuizhi.agent.model.po.AgentSessionMeta;
import com.cuizhi.agent.model.vo.SessionVO;

/**
 * @Author thpaperman
 * @Description AI会话元数据接口
 * @Date 2026/4/21
 * @Version 1.0
 */
public interface AgentSessionService extends IService<AgentSessionMeta> {

    SessionVO createSession();
}
