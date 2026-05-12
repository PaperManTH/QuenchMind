package com.cuizhi.auth.service;

import com.alibaba.fastjson2.JSONObject;

public interface WechatOAuthClient {

    JSONObject exchangeToken(String code);

    JSONObject fetchUserInfo(String accessToken, String openid);
}

