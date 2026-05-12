package com.cuizhi.auth.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.cuizhi.auth.service.WechatOAuthClient;
import com.cuizhi.core.exception.auth.AuthThirdPartyException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class WechatOAuthClientImpl implements WechatOAuthClient {

    @Value("${wechat.oauth.app-id:}")
    private String appId;

    @Value("${wechat.oauth.app-secret:}")
    private String appSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public JSONObject exchangeToken(String code) {
        String url = UriComponentsBuilder.fromHttpUrl("https://api.weixin.qq.com/sns/oauth2/access_token")
                .queryParam("appid", appId)
                .queryParam("secret", appSecret)
                .queryParam("code", code)
                .queryParam("grant_type", "authorization_code")
                .toUriString();
        String body = restTemplate.getForObject(url, String.class);
        JSONObject json = JSONObject.parseObject(body);
        if (json.containsKey("errcode")) {
            throw new AuthThirdPartyException("微信换取 access_token 失败");
        }
        return json;
    }

    @Override
    public JSONObject fetchUserInfo(String accessToken, String openid) {
        String url = UriComponentsBuilder.fromHttpUrl("https://api.weixin.qq.com/sns/userinfo")
                .queryParam("access_token", accessToken)
                .queryParam("openid", openid)
                .queryParam("lang", "zh_CN")
                .toUriString();
        String body = restTemplate.getForObject(url, String.class);
        JSONObject json = JSONObject.parseObject(body);
        if (json.containsKey("errcode")) {
            throw new AuthThirdPartyException("微信获取用户信息失败");
        }
        return json;
    }
}
