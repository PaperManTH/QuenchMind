package com.cuizhi.resource.controller;

import com.cuizhi.core.model.ResponseResult;
import com.cuizhi.resource.model.dto.UploadResult;
import com.cuizhi.resource.service.ResourceUploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * @Author thpaperman
 * @Description 文件上传控制器（分片上传 + 秒传 + 断点续传 + 合并）
 * @Date 2026/7/7
 * @Version 1.1
 */
@Slf4j
@RestController
@RequestMapping("/resource/upload")
@AllArgsConstructor
@Tag(name = "资源上传", description = "分片上传 + 秒传 + 断点续传 + 合并")
public class UploadController {

    private final ResourceUploadService uploadService;

    @GetMapping("/check")
    @Operation(summary = "秒传检查")
    public ResponseResult<Map<String, Object>> check(@RequestParam String md5) {
        String resourceId = uploadService.checkByMd5(md5);
        return ResponseResult.success(Map.of(
                "uploaded", resourceId != null,
                "resourceId", resourceId != null ? resourceId : ""
        ));
    }

    @GetMapping("/progress")
    @Operation(summary = "断点续传 — 查询已上传分片")
    public ResponseResult<Map<String, Object>> progress(@RequestParam String md5) {
        Set<Integer> done = uploadService.getUploadedChunks(md5);
        return ResponseResult.success(Map.of("doneChunks", done));
    }

    @PostMapping("/chunk")
    @Operation(summary = "上传分片")
    public ResponseResult<Void> uploadChunk(
            @RequestParam String md5,
            @RequestParam int index,
            @RequestParam int total,
            @RequestParam MultipartFile chunk) throws IOException {
        String ext = getExtension(chunk.getOriginalFilename());
        uploadService.uploadChunk(md5, ext, index, total, chunk.getBytes());
        return ResponseResult.success();
    }

    private String getExtension(String fileName) {
        if (fileName == null) return "";
        int dot = fileName.lastIndexOf('.');
        return dot > 0 ? fileName.substring(dot + 1).toLowerCase() : "";
    }

    @PostMapping("/merge")
    @Operation(summary = "合并分片")
    public ResponseResult<UploadResult> merge(
            @RequestParam String md5,
            @RequestParam String filename,
            @RequestParam String userId) {
        UploadResult result = uploadService.mergeChunks(md5, filename, userId);
        return ResponseResult.success(result);
    }
}
