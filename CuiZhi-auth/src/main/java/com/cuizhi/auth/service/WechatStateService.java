package com.cuizhi.auth.service;

public interface WechatStateService {

    String createState(String redirect);

    /**
     * 读取并删除 state 对应的 redirect。
     */
    String consumeRedirect(String state);
}

