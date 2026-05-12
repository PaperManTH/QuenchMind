package com.cuizhi.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cuizhi.auth.mapper.CzUserMapper;
import com.cuizhi.auth.model.po.CzUser;
import com.cuizhi.auth.service.WechatUserBindService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class WechatUserBindServiceImpl implements WechatUserBindService {

    @Autowired
    private CzUserMapper czUserMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public CzUser upsertByWechat(String openid, String unionid, String nickname, String avatar) {
        CzUser czUser = czUserMapper.selectOne(new LambdaQueryWrapper<CzUser>().eq(CzUser::getWechatOpenid, openid));
        if (czUser == null) {
            czUser = new CzUser();
            czUser.setWechatOpenid(openid);
            czUser.setWechatUnionid(unionid);
            czUser.setUserName("wx_" + openid);
            czUser.setLoginType("WX");
            czUser.setNickName(nickname);
            czUser.setUserAvatar(avatar);
            czUser.setStatus(1);
            czUser.setPasswordHash(passwordEncoder.encode(UUID.randomUUID().toString()));
            czUserMapper.insert(czUser);
            return czUser;
        }

        boolean changed = false;
        if (!StringUtils.equals(czUser.getWechatUnionid(), unionid)) {
            czUser.setWechatUnionid(unionid);
            changed = true;
        }
        if (StringUtils.isNotBlank(nickname) && !StringUtils.equals(czUser.getNickName(), nickname)) {
            czUser.setNickName(nickname);
            changed = true;
        }
        if (StringUtils.isNotBlank(avatar) && !StringUtils.equals(czUser.getUserAvatar(), avatar)) {
            czUser.setUserAvatar(avatar);
            changed = true;
        }
        if (changed) {
            czUserMapper.updateById(czUser);
        }
        return czUser;
    }
}

