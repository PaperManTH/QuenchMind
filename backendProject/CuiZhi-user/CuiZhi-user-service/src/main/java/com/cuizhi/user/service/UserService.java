package com.cuizhi.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cuizhi.user.model.dto.ChangePasswordDto;
import com.cuizhi.user.model.dto.SetPasswordDto;
import com.cuizhi.user.model.dto.UserInfo;
import com.cuizhi.user.model.po.CzUser;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: thpaperman
 * @Date: 2026/5/13
 * @Description: 用户账号表 服务类
 * @Version: 1.0
 */
public interface UserService extends IService<CzUser> {

    /**
     * 获取用户基本信息
     * @param userId 用户id
     * @return 用户基本信息
     */
    UserInfo getUserProfile(@NotBlank String userId);

    /**
     * 修改个人信息
     * @param userInfo 个人信息
     * @param userId 用户id
     */
    void modifyInfo(UserInfo userInfo, String userId);

    /**
     * 设置初始密码（邮箱/GitHub登录后首次设密码，无需旧密码）
     */
    void setPassword(SetPasswordDto dto, String userId);

    /**
     * 修改账户密码（需旧密码验证）
     */
    void changePassword(@Valid ChangePasswordDto changePassword, String userId);

    /**
     * 上传文件
     * @param file 头像文件
     * @param userId 用户id
     * @return url地址
     */
    String uploadFile(MultipartFile file, String userId);
}
