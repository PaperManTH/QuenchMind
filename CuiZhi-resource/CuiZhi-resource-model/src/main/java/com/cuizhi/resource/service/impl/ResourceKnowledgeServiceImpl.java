package com.cuizhi.resource.service.impl;

import com.cuizhi.resource.model.po.ResourceKnowledge;
import com.cuizhi.resource.mapper.ResourceKnowledgeMapper;
import com.cuizhi.resource.service.ResourceKnowledgeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * AI提炼的知识点表（结构化知识） 服务实现类
 * </p>
 *
 * @author cuizhi
 */
@Slf4j
@Service
public class ResourceKnowledgeServiceImpl extends ServiceImpl<ResourceKnowledgeMapper, ResourceKnowledge> implements ResourceKnowledgeService {

}
