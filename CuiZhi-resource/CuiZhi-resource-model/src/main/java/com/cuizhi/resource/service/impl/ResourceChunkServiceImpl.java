package com.cuizhi.resource.service.impl;

import com.cuizhi.resource.model.po.ResourceChunk;
import com.cuizhi.resource.mapper.ResourceChunkMapper;
import com.cuizhi.resource.service.ResourceChunkService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * 原始文本切块表（用于向量检索） 服务实现类
 * </p>
 *
 * @author cuizhi
 */
@Slf4j
@Service
public class ResourceChunkServiceImpl extends ServiceImpl<ResourceChunkMapper, ResourceChunk> implements ResourceChunkService {

}
