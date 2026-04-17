package com.cuizhi.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cuizhi.auth.mapper.CzUserMapper;
import com.cuizhi.auth.model.po.CzUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * 标准 Spring Security 用户加载：根据用户名从数据库加载用户。
 */
@Component
public class UserServiceImpl implements UserDetailsService {

    @Autowired
    private CzUserMapper czUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtils.isBlank(username)) {
            throw new UsernameNotFoundException("用户名不能为空");
        }

        CzUser czUser = czUserMapper.selectOne(
                new LambdaQueryWrapper<CzUser>().eq(CzUser::getUserName, username)
        );
        if (czUser == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        String[] authorities = {};
        return User.withUsername(czUser.getUserName())
                .password(czUser.getPasswordHash())
                .authorities(authorities)
                .build();
    }
}

