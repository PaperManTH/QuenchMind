package com.cuizhi.auth.service;

import com.cuizhi.auth.model.po.CzUser;

public interface WechatLoginService {

    String buildAuthorizeUrl(String state);

    CzUser handleCallbackAndLogin(String code);
}

