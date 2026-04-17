package com.cuizhi.resource.service.impl;

import com.cuizhi.resource.model.po.Resource;
import com.cuizhi.resource.mapper.ResourceMapper;
import com.cuizhi.resource.service.ResourceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * 学习资料表 服务实现类
 * </p>
 *
 * @author cuizhi
 */
@Slf4j
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements ResourceService {

}
