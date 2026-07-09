package com.cuizhi.auth.service;

import com.cuizhi.auth.model.dto.AuthResultDto;

/**
 * @Author thpaperman
 * @Description GitHub OAuth2 登录服务
 * @Date 2026/7/7
 * @Version 1.0
 */
public interface GithubLoginService {

    /**
     * 构建 GitHub 授权 URL
     */
    String buildAuthorizeUrl(String state);

    /**
     * 处理 GitHub 回调，完成登录（登录即注册），返回 JWT Token
     */
    AuthResultDto handleCallback(String code);
}
