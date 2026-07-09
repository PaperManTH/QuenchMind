package com.cuizhi.auth.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cuizhi.auth.mapper.CzUserMapper;
import com.cuizhi.auth.model.dto.AuthResultDto;
import com.cuizhi.auth.model.po.CzUser;
import com.cuizhi.auth.service.GithubLoginService;
import com.cuizhi.auth.utils.JwtUtils;
import com.cuizhi.core.exception.CuiZhiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

/**
 * GitHub OAuth2 登录实现 — 登录即注册
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GithubLoginServiceImpl implements GithubLoginService {

    @Value("${github.oauth.client-id:}")
    private String clientId;

    @Value("${github.oauth.client-secret:}")
    private String clientSecret;

    @Value("${github.oauth.redirect-uri:}")
    private String redirectUri;

    private final CzUserMapper czUserMapper;

    private final JwtUtils jwtUtils;

    private final PasswordEncoder passwordEncoder;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String buildAuthorizeUrl(String state) {
        if (!StringUtils.hasText(clientId) || !StringUtils.hasText(redirectUri)) {
            throw new CuiZhiException(500, "GitHub 登录未配置 client-id/redirect-uri");
        }
        return "https://github.com/login/oauth/authorize"
                + "?client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&state=" + state
                + "&scope=user:email";
    }

    @Override
    public AuthResultDto handleCallback(String code) {
        String accessToken = exchangeCodeForToken(code);
        JSONObject githubUser = fetchUserInfo(accessToken);
        String email = fetchPrimaryEmail(accessToken);

        String githubId = githubUser.getString("id");
        String login = githubUser.getString("login");
        String name = githubUser.getString("name");
        String avatar = githubUser.getString("avatar_url");
        if (!StringUtils.hasText(name)) {
            name = login;
        }

        CzUser user = upsertUser(githubId, login, name, avatar, email);
        AuthResultDto tokens = jwtUtils.generateTokens(user.getUserName(), user.getId());
        tokens.setUserId(user.getId());
        tokens.setUsername(user.getUserName());
        tokens.setEmail(user.getUserEmail());
        tokens.setNickName(user.getNickName());
        tokens.setAvatar(user.getUserAvatar());
        return tokens;
    }

    private String exchangeCodeForToken(String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("code", code);
        params.add("redirect_uri", redirectUri);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        ResponseEntity<String> resp = restTemplate.exchange(
                "https://github.com/login/oauth/access_token",
                HttpMethod.POST,
                new HttpEntity<>(params, headers),
                String.class);

        JSONObject json = JSON.parseObject(resp.getBody());
        String token = json.getString("access_token");
        if (!StringUtils.hasText(token)) {
            log.error("GitHub token exchange failed: {}", resp.getBody());
            throw new CuiZhiException(401, "GitHub 授权失败");
        }
        return token;
    }

    private JSONObject fetchUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        ResponseEntity<String> resp = restTemplate.exchange(
                "https://api.github.com/user",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class);

        return JSON.parseObject(resp.getBody());
    }

    private String fetchPrimaryEmail(String accessToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

            ResponseEntity<String> resp = restTemplate.exchange(
                    "https://api.github.com/user/emails",
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    String.class);

            JSONArray emails = JSON.parseArray(resp.getBody());
            if (emails != null) {
                for (int i = 0; i < emails.size(); i++) {
                    JSONObject e = emails.getJSONObject(i);
                    if (Boolean.TRUE.equals(e.getBoolean("primary")) && Boolean.TRUE.equals(e.getBoolean("verified"))) {
                        return e.getString("email");
                    }
                }
            }
        } catch (Exception ex) {
            log.warn("获取 GitHub 邮箱失败: {}", ex.getMessage());
        }
        return null;
    }

    private CzUser upsertUser(String githubId, String login, String name, String avatar, String email) {
        CzUser user = czUserMapper.selectOne(new LambdaQueryWrapper<CzUser>()
                .eq(CzUser::getOauthProvider, "GITHUB")
                .eq(CzUser::getOauthProviderId, githubId));

        if (user == null) {
            user = new CzUser();
            user.setUserName("gh_" + login);
            user.setOauthProvider("GITHUB");
            user.setOauthProviderId(githubId);
            user.setLoginType("GITHUB");
            user.setNickName(name);
            user.setUserAvatar(avatar);
            user.setUserEmail(email);
            user.setStatus(1);
            user.setPasswordHash(passwordEncoder.encode(UUID.randomUUID().toString()));
            czUserMapper.insert(user);
            log.info("GitHub 用户注册成功: gh_{}, id={}", login, user.getId());
        } else {
            boolean changed = false;
            if (StringUtils.hasText(name) && !name.equals(user.getNickName())) {
                user.setNickName(name);
                changed = true;
            }
            if (StringUtils.hasText(avatar) && !avatar.equals(user.getUserAvatar())) {
                user.setUserAvatar(avatar);
                changed = true;
            }
            if (StringUtils.hasText(email) && !email.equals(user.getUserEmail())) {
                user.setUserEmail(email);
                changed = true;
            }
            if (changed) {
                czUserMapper.updateById(user);
            }
        }
        return user;
    }
}
