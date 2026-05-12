package com.cuizhi.auth.service;

public interface EmailCodeService {

    void sendLoginCode(String email);

    boolean verifyLoginCode(String email, String code);
}

