package com.cuizhi.user.controller;

import com.cuizhi.core.annotation.CurrentUserId;
import com.cuizhi.core.model.ResponseResult;
import com.cuizhi.user.service.GroupMemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 小组成员管理接口
 */
@Tag(name = "小组成员管理接口", description = "小组成员管理接口")
@Slf4j
@RestController
@RequestMapping("/group/member")
@AllArgsConstructor
public class MemberController {

    private final GroupMemberService groupMemberService;

    @PostMapping("/join")
    @Operation(summary = "加入学习小组")
    public ResponseResult<Void> joinGroup(@RequestParam @NotBlank String groupId,
                                          @CurrentUserId String userId) {
        groupMemberService.joinGroup(groupId, userId);
        return ResponseResult.success();
    }

    @PostMapping("/quit")
    @Operation(summary = "退出学习小组")
    public ResponseResult<Void> quitGroup(@RequestParam @NotBlank String groupId,
                                          @CurrentUserId String userId) {
        groupMemberService.quitGroup(groupId, userId);
        return ResponseResult.success();
    }

    @DeleteMapping("/{groupId}/member/{userId}")
    @Operation(summary = "移除学习小组成员")
    public ResponseResult<Void> removeMember(@PathVariable @NotBlank String groupId,
                                             @PathVariable String userId,
                                             @CurrentUserId String currentUserId) {
        groupMemberService.removeMember(groupId, userId, currentUserId);
        return ResponseResult.success();
    }
}
