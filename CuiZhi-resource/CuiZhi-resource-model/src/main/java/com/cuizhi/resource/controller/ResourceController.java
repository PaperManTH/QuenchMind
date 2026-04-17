package com.cuizhi.resource.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cuizhi.resource.service.ResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * 学习资料表 前端控制器
 * </p>
 *
 * @author cuizhi
 */
@Slf4j
@RestController
@RequestMapping("resource")
public class ResourceController {

    @Autowired
    private ResourceService  resourceService;

    /**
     * 1️⃣ 资源上传（文档/视频）
     * 2️⃣ 资源解析（PDF/Word）
     * 3️⃣ 资源列表查询
     * 4️⃣ 资源详情获取
     * 5️⃣ 资源分类管理
     * 6️⃣ 资源标签管理
     * 7️⃣ 资源搜索（关键字）
     * 8️⃣ 资源收藏管理
     * 9️⃣ 资源评分管理
     * 🔟 资源向量化（RAG预处理）
     */
}
