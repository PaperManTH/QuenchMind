package com.cuizhi.resource.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cuizhi.resource.service.ResourceKnowledgeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * AI提炼的知识点表（结构化知识） 前端控制器
 * </p>
 *
 * @author cuizhi
 */
@Slf4j
@RestController
@RequestMapping("resourceKnowledge")
public class ResourceKnowledgeController {

    @Autowired
    private ResourceKnowledgeService  resourceKnowledgeService;
}
