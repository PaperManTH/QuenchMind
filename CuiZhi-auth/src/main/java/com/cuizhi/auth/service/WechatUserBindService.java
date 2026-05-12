package com.cuizhi.auth.service;

import com.cuizhi.auth.model.po.CzUser;

public interface WechatUserBindService {

    CzUser upsertByWechat(String openid, String unionid, String nickname, String avatar);
}

