package com.cuizhi.resource.controller;

import com.cuizhi.core.model.ResponseResult;
import com.cuizhi.resource.service.ResourceStatusService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Author thpaperman
 * @Description Python AI 引擎回调接口 — 处理完成后回写数据库
 * @Date 2026/7/7
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/resource/callback")
public class ProcessCallbackController {

    private final ResourceStatusService statusService;

    public ProcessCallbackController(ResourceStatusService statusService) {
        this.statusService = statusService;
    }

    @PutMapping("/{resourceId}")
    @Operation(summary = "Python 处理完成回调", description = "更新资源解析结果、摘要和向量状态")
    public ResponseResult<Void> onProcessComplete(
            @PathVariable String resourceId,
            @RequestBody Map<String, Object> body) {
        String summary = (String) body.getOrDefault("summary", "");
        String errorMsg = (String) body.getOrDefault("error", null);
        int chunkCount = body.containsKey("chunk_count") ? ((Number) body.get("chunk_count")).intValue() : 0;
        int vectorStatus = body.containsKey("vector_status") ? ((Number) body.get("vector_status")).intValue() : 0;

        statusService.updateParseResult(resourceId, summary, chunkCount, errorMsg);
        statusService.updateVectorStatus(resourceId, vectorStatus);

        log.info("回调处理完成: resourceId={}, chunks={}, vectorStatus={}", resourceId, chunkCount, vectorStatus);
        return ResponseResult.success();
    }
}
