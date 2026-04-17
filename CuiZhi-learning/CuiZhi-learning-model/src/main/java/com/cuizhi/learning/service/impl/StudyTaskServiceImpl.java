package com.cuizhi.learning.service.impl;

import com.cuizhi.learning.model.po.StudyTask;
import com.cuizhi.learning.mapper.StudyTaskMapper;
import com.cuizhi.learning.service.StudyTaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * 学习任务表 服务实现类
 * </p>
 *
 * @author cuizhi
 */
@Slf4j
@Service
public class StudyTaskServiceImpl extends ServiceImpl<StudyTaskMapper, StudyTask> implements StudyTaskService {

}
