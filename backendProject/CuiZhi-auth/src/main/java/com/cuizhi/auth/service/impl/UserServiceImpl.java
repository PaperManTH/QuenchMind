package com.cuizhi.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cuizhi.auth.mapper.CzUserMapper;
import com.cuizhi.auth.model.po.CzUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * Spring Security UserDetailsService 实现
 */
@Component
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserDetailsService {

    private final CzUserMapper czUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtils.isBlank(username)) {
            throw new UsernameNotFoundException("用户名不能为空");
        }

        CzUser czUser = czUserMapper.selectOne(
                new LambdaQueryWrapper<CzUser>()
                        .eq(CzUser::getUserName, username)
                        .or()
                        .eq(CzUser::getUserPhone, username));

        if (czUser == null) {
            log.info("用户不存在: {}", username);
            throw new UsernameNotFoundException("用户不存在");
        }

        return User.withUsername(czUser.getUserName())
                .password(czUser.getPasswordHash())
                .build();
    }
}
