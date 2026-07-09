package com.cuizhi.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cuizhi.core.exception.user.UserBadRequestException;
import com.cuizhi.core.exception.user.UserForbiddenException;
import com.cuizhi.feign.ResourceFeignClient;
import com.cuizhi.user.mapper.UserMapper;
import com.cuizhi.user.model.dto.ChangePasswordDto;
import com.cuizhi.user.model.dto.SetPasswordDto;
import com.cuizhi.user.model.dto.UserInfo;
import com.cuizhi.user.model.po.CzUser;
import com.cuizhi.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用户服务实现。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, CzUser> implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final ResourceFeignClient resourceFeignClient;

    @Override
    public UserInfo getUserProfile(String userId) {
        CzUser user = getById(userId);
        if (user == null) {
            throw new UserBadRequestException("用户不存在");
        }
        user.setPasswordHash(null);
        return BeanUtil.copyProperties(user, UserInfo.class);
    }

    @Override
    public void modifyInfo(UserInfo userInfo, String userId) {
        if (userInfo == null) {
            throw new UserBadRequestException("参数不能为空");
        }
        if (!userId.equals(userInfo.getUserId())) {
            throw new UserForbiddenException("非法操作");
        }
        CzUser user = getById(userId);
        if (user == null) {
            throw new UserBadRequestException("用户不存在");
        }

        // 只允许修改这些字段，防止注入敏感字段
        user.setNickName(userInfo.getNickName());
        user.setUserAvatar(userInfo.getUserAvatar());
        user.setUserGender(userInfo.getUserGender());
        user.setBirthday(userInfo.getBirthday());
        user.setUserPhone(userInfo.getUserPhone());
        user.setUserEmail(userInfo.getUserEmail());

        if (!updateById(user)) {
            throw new UserBadRequestException("修改用户信息失败");
        }
    }

    @Override
    public void setPassword(SetPasswordDto dto, String userId) {
        CzUser user = getById(userId);
        if (user == null) {
            throw new UserBadRequestException("用户不存在");
        }
        user.setPasswordHash(passwordEncoder.encode(dto.getNewPassword()));
        if (!updateById(user)) {
            throw new UserBadRequestException("设置密码失败");
        }
    }

    @Override
    public void changePassword(ChangePasswordDto changePassword, String userId) {
        if (changePassword == null) {
            throw new UserBadRequestException("参数不能为空");
        }
        String oldPassword = changePassword.getOldPassword();
        String newPassword = changePassword.getNewPassword();
        if (oldPassword != null && oldPassword.equals(newPassword)) {
            throw new UserBadRequestException("新旧密码不能相同");
        }

        CzUser user = getById(userId);
        if (user == null) {
            throw new UserBadRequestException("用户不存在");
        }

        if (!passwordEncoder.matches(oldPassword, user.getPasswordHash())) {
            throw new UserBadRequestException("旧密码验证失败");
        }

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        boolean update = updateById(user);
        if (!update) {
            throw new UserBadRequestException("修改密码失败，请稍后重试");
        }
    }

    @Override
    public String uploadFile(MultipartFile file, String userId) {
        return resourceFeignClient.uploadFile(file, "/user/" + userId + "/avatar").getData();
    }
}

