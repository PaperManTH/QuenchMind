package com.cuizhi.auth.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @Author: thpaperman
 * @Date: 2026/5/15
 * @Description: 邮箱验证码认证 Token
 * @Version: 1.0
 */
public class EmailCodeAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;
    private final Object credentials;

    public EmailCodeAuthenticationToken(String email, String code) {
        super(null);
        this.principal = email;
        this.credentials = code;
        setAuthenticated(false);
    }

    public EmailCodeAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}

