package com.cuizhi.learning.service.impl;

import com.cuizhi.learning.model.po.WrongQuestion;
import com.cuizhi.learning.mapper.WrongQuestionMapper;
import com.cuizhi.learning.service.WrongQuestionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * 错题复习表 服务实现类
 * </p>
 *
 * @author cuizhi
 */
@Slf4j
@Service
public class WrongQuestionServiceImpl extends ServiceImpl<WrongQuestionMapper, WrongQuestion> implements WrongQuestionService {

}
