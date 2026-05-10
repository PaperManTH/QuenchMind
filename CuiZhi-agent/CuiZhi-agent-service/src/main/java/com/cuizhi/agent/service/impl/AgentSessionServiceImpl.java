package com.cuizhi.agent.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cuizhi.agent.mapper.AgentSessionMetaMapper;
import com.cuizhi.agent.model.enums.SessionStatus;
import com.cuizhi.agent.model.po.AgentSessionMeta;
import com.cuizhi.agent.config.SessionProperties;
import com.cuizhi.agent.model.vo.SessionVO;
import com.cuizhi.agent.service.AgentSessionService;
import com.cuizhi.core.exception.agent.SessionCreateException;
import com.cuizhi.core.model.context.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @Author thpaperman
 * @Description AI会话接口实现类
 * @Date 2026/4/21
 * @Version 1.0
 */
@Slf4j
@Service
public class AgentSessionServiceImpl extends ServiceImpl<AgentSessionMetaMapper, AgentSessionMeta> implements AgentSessionService {

    @Autowired
    private SessionProperties sessionProperties;

    @Override
    public SessionVO createSession() {
        SessionVO sessionVO = BeanUtil.toBean(sessionProperties, SessionVO.class);
        // 生成随机 UUID 作为会话 ID
        sessionVO.setSessionId(IdUtil.simpleUUID());

        // 存入数据库
        AgentSessionMeta sessionMeta = AgentSessionMeta.builder()
                .sessionId(sessionVO.getSessionId())
                .userId(UserContext.getCurrentUserId())
                .status(SessionStatus.NORMAL.getCode())
                .createdAt(LocalDateTime.now())
                .build();
        boolean save = save(sessionMeta);
        if(!save) {
            throw new SessionCreateException("创建会话失败");
        }
        BeanUtil.copyProperties(sessionMeta, sessionVO);
        // 返回会话信息
        return sessionVO;
    }
}
