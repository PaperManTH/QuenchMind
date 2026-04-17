package com.cuizhi.learning.service.impl;

import com.cuizhi.learning.model.po.StudyPlan;
import com.cuizhi.learning.mapper.StudyPlanMapper;
import com.cuizhi.learning.service.StudyPlanService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * 学习计划表 服务实现类
 * </p>
 *
 * @author cuizhi
 */
@Slf4j
@Service
public class StudyPlanServiceImpl extends ServiceImpl<StudyPlanMapper, StudyPlan> implements StudyPlanService {

}
