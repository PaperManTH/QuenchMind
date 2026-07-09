package com.cuizhi.user.controller;

import com.cuizhi.core.annotation.CurrentUserId;
import com.cuizhi.core.model.ResponseResult;
import com.cuizhi.core.model.context.UserContext;
import com.cuizhi.user.model.dto.ChangePasswordDto;
import com.cuizhi.user.model.dto.SetPasswordDto;
import com.cuizhi.user.model.dto.UserInfo;
import com.cuizhi.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用户接口
 */
@Tag(name = "用户接口", description = "用户接口")
@Slf4j
@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    @Operation(summary = "获取用户信息")
    public ResponseResult<UserInfo> getUserProfile() {
        String userId = UserContext.getCurrentUserId();
        UserInfo userInfo = userService.getUserProfile(userId);
        return ResponseResult.success(userInfo);
    }

    @PostMapping("/modifyInfo")
    @Operation(summary = "修改个人信息")
    public ResponseResult<Void> modifyInfo(@RequestBody @Valid UserInfo userInfo,
                                           @CurrentUserId String userId) {
        userService.modifyInfo(userInfo, userId);
        return ResponseResult.success();
    }

    @PostMapping("/setPassword")
    @Operation(summary = "设置密码（邮箱/GitHub注册后首次设定）")
    public ResponseResult<Void> setPassword(@RequestBody @Valid SetPasswordDto dto,
                                            @CurrentUserId String userId) {
        userService.setPassword(dto, userId);
        return ResponseResult.success();
    }

    @PostMapping("/modifyInfo/changePwd")
    @Operation(summary = "修改密码（需旧密码）")
    public ResponseResult<Void> changePassword(@RequestBody @Valid ChangePasswordDto changePassword,
                                               @CurrentUserId String userId) {
        userService.changePassword(changePassword, userId);
        return ResponseResult.success();
    }

    @PostMapping("/upload/avatar")
    @Operation(summary = "上传用户头像")
    public ResponseResult<String> uploadAvatar(@RequestParam("file") MultipartFile file,
                                               @CurrentUserId String userId) {
        return ResponseResult.success(userService.uploadFile(file, userId));
    }
}
