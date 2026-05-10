package com.cuizhi.agent.controller;

import com.cuizhi.agent.model.vo.SessionVO;
import com.cuizhi.agent.service.AgentSessionService;
import com.cuizhi.core.model.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author thpaperman
 * @Description 会话管理 Controller
 * @Date 2026/4/21
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("session")
public class AgentSessionController {

    @Autowired
    private AgentSessionService sessionService;

    @PostMapping("create")
    public ResponseResult<SessionVO> createSession() {
        log.info("创建会话请求");
        // 调用 Service 层创建会话
        SessionVO result = sessionService.createSession();
        return ResponseResult.success(result, "会话创建成功");
    }

    @PostMapping("delete")
    public ResponseResult<Void> deleteSession() {
        log.info("删除会话请求");
        return ResponseResult.success(null, "会话删除成功");
    }

    @PostMapping("query")
    public ResponseResult<SessionVO> querySession() {
        log.info("查询会话请求");
        return ResponseResult.success(null, "会话查询成功");
    }

}
