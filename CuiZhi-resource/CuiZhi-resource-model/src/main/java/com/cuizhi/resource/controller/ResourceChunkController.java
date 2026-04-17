package com.cuizhi.resource.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cuizhi.resource.service.ResourceChunkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * 原始文本切块表（用于向量检索） 前端控制器
 * </p>
 *
 * @author cuizhi
 */
@Slf4j
@RestController
@RequestMapping("resourceChunk")
public class ResourceChunkController {

    @Autowired
    private ResourceChunkService  resourceChunkService;
}
