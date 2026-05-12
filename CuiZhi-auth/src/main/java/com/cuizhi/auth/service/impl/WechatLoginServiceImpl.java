package com.cuizhi.auth.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.cuizhi.auth.model.po.CzUser;
import com.cuizhi.auth.service.WechatLoginService;
import com.cuizhi.auth.service.WechatOAuthClient;
import com.cuizhi.auth.service.WechatUserBindService;
import com.cuizhi.core.exception.auth.AuthException;
import com.cuizhi.core.exception.auth.AuthBadRequestException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class WechatLoginServiceImpl implements WechatLoginService {

    @Value("${wechat.oauth.app-id:}")
    private String appId;

    @Value("${wechat.oauth.redirect-uri:}")
    private String redirectUri;

    @Autowired
    private WechatOAuthClient wechatOAuthClient;

    @Autowired
    private WechatUserBindService wechatUserBindService;

    @Override
    public String buildAuthorizeUrl(String state) {
        if (StringUtils.isBlank(appId) || StringUtils.isBlank(redirectUri)) {
            throw new AuthBadRequestException("微信登录未配置 app-id/redirect-uri");
        }
        String encodedRedirectUri = URLEncoder.encode(redirectUri, StandardCharsets.UTF_8);
        return "https://open.weixin.qq.com/connect/qrconnect"
                + "?appid=" + appId
                + "&redirect_uri=" + encodedRedirectUri
                + "&response_type=code"
                + "&scope=snsapi_login"
                + "&state=" + state
                + "#wechat_redirect";
    }

    @Override
    public CzUser handleCallbackAndLogin(String code) {
        JSONObject token = wechatOAuthClient.exchangeToken(code);
        String openid = token.getString("openid");
        String unionid = token.getString("unionid");
        String accessToken = token.getString("access_token");
        if (StringUtils.isBlank(openid) || StringUtils.isBlank(accessToken)) {
            throw new AuthException(401, "微信授权失败");
        }

        JSONObject userInfo = wechatOAuthClient.fetchUserInfo(accessToken, openid);
        String nickname = userInfo.getString("nickname");
        String avatar = userInfo.getString("headimgurl");

        CzUser czUser = wechatUserBindService.upsertByWechat(openid, unionid, nickname, avatar);

        UserDetails userDetails = User.withUsername(czUser.getUserName())
                .password(czUser.getPasswordHash())
                .authorities(new String[] {})
                .build();
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
        );

        return czUser;
    }
}
