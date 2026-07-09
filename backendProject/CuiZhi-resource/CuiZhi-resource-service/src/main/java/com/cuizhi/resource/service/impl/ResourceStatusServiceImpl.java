package com.cuizhi.resource.service.impl;

import com.cuizhi.resource.mapper.ResourceMapper;
import com.cuizhi.resource.model.po.Resource;
import com.cuizhi.resource.service.ResourceStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @Author thpaperman
 * @Description 资源状态管理实现
 * @Date 2026/7/7
 * @Version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceStatusServiceImpl implements ResourceStatusService {

    private final ResourceMapper resourceMapper;

    @Override
    public void updateParseResult(String resourceId, String summary, int chunkCount, String errorMsg) {
        Resource resource = resourceMapper.selectById(resourceId);
        if (resource == null) {
            log.error("资源不存在: {}", resourceId);
            return;
        }
        if (errorMsg != null && !errorMsg.isEmpty()) {
            resource.setParseStatus(3);
            resource.setParseErrorMsg(errorMsg);
        } else {
            resource.setParseStatus(2);
            resource.setDescription(summary);
        }
        resource.setUpdatedAt(LocalDateTime.now());
        resourceMapper.updateById(resource);
        log.info("资源状态更新: {} → {}", resourceId, errorMsg != null ? "失败" : "成功/分块" + chunkCount);
    }

    @Override
    public void updateVectorStatus(String resourceId, int status) {
        Resource resource = resourceMapper.selectById(resourceId);
        if (resource == null) return;
        resource.setVectorStatus(status);
        resource.setUpdatedAt(LocalDateTime.now());
        resourceMapper.updateById(resource);
    }
}
