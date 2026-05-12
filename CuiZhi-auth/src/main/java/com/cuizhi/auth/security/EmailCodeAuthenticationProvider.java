package com.cuizhi.auth.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cuizhi.auth.mapper.CzUserMapper;
import com.cuizhi.auth.model.po.CzUser;
import com.cuizhi.auth.service.EmailCodeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class EmailCodeAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private EmailCodeService emailCodeService;

    @Autowired
    private CzUserMapper czUserMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!(authentication instanceof EmailCodeAuthenticationToken token)) {
            return null;
        }

        String email = token.getPrincipal() == null ? null : token.getPrincipal().toString();
        String code = token.getCredentials() == null ? null : token.getCredentials().toString();
        if (StringUtils.isBlank(email) || StringUtils.isBlank(code)) {
            throw new BadCredentialsException("邮箱或验证码不能为空");
        }

        boolean ok = emailCodeService.verifyLoginCode(email, code);
        if (!ok) {
            throw new BadCredentialsException("邮箱验证码错误或已过期");
        }

        CzUser czUser = czUserMapper.selectOne(new LambdaQueryWrapper<CzUser>().eq(CzUser::getUserEmail, email));
        if (czUser == null) {
            czUser = new CzUser();
            czUser.setUserEmail(email);
            czUser.setUserName(email);
            czUser.setLoginType("EMAIL");
            czUser.setStatus(1);
            czUser.setPasswordHash(passwordEncoder.encode(UUID.randomUUID().toString()));
            czUserMapper.insert(czUser);
        }

        UserDetails userDetails = User.withUsername(czUser.getUserName())
                .password(czUser.getPasswordHash())
                .authorities(new String[] {})
                .build();

        return new EmailCodeAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return EmailCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

