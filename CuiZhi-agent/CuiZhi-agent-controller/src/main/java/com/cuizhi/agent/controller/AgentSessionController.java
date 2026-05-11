package com.cuizhi.agent.controller;

import com.cuizhi.agent.model.vo.SessionVO;
import com.cuizhi.agent.service.AgentSessionService;
import com.cuizhi.core.model.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/session")
@Tag(name = "会话管理", description = "会话管理")
public class AgentSessionController {

    @Autowired
    private AgentSessionService sessionService;

    @PostMapping("create")
    @Operation(summary = "创建会话", description = "创建会话")
    public ResponseResult<SessionVO> createSession() {
        log.info("创建会话请求");
        // 调用 Service 层创建会话
        SessionVO result = sessionService.createSession();
        return ResponseResult.success(result, "会话创建成功");
    }

    @PostMapping("delete")
    @Operation(summary = "删除会话", description = "删除会话")
    public ResponseResult<Void> deleteSession() {
        log.info("删除会话请求");
        return ResponseResult.success(null, "会话删除成功");
    }

    @PostMapping("query")
    @Operation(summary = "查询会话", description = "查询会话")
    public ResponseResult<SessionVO> querySession() {
        log.info("查询会话请求");
        return ResponseResult.success(null, "会话查询成功");
    }

}
