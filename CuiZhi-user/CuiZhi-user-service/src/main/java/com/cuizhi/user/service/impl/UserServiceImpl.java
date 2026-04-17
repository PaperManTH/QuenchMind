package com.cuizhi.user.service.impl;

import com.cuizhi.user.model.po.User;
import com.cuizhi.user.mapper.UserMapper;
import com.cuizhi.user.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * 用户账号表 服务实现类
 * </p>
 *
 * @author thpaperman
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
