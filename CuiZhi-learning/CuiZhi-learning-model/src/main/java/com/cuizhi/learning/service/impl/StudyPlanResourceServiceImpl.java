package com.cuizhi.learning.service.impl;

import com.cuizhi.learning.model.po.StudyPlanResource;
import com.cuizhi.learning.mapper.StudyPlanResourceMapper;
import com.cuizhi.learning.service.StudyPlanResourceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * 学习计划-资料关联表（多对多） 服务实现类
 * </p>
 *
 * @author cuizhi
 */
@Slf4j
@Service
public class StudyPlanResourceServiceImpl extends ServiceImpl<StudyPlanResourceMapper, StudyPlanResource> implements StudyPlanResourceService {

}
