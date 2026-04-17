package com.cuizhi.user.service.impl;

import com.cuizhi.user.model.po.Role;
import com.cuizhi.user.mapper.RoleMapper;
import com.cuizhi.user.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author thpaperman
 */
@Slf4j
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

}
