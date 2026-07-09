package com.cuizhi.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cuizhi.core.exception.user.UserBadRequestException;
import com.cuizhi.core.exception.user.UserConflictException;
import com.cuizhi.user.mapper.GroupMemberMapper;
import com.cuizhi.user.model.enums.GroupMemberRole;
import com.cuizhi.user.model.po.CzGroupMember;
import com.cuizhi.user.model.po.CzStudyGroup;
import com.cuizhi.user.service.GroupMemberService;
import com.cuizhi.user.service.StudyGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 小组成员服务实现
 */
@Slf4j
@Service
public class GroupMemberServiceImpl extends ServiceImpl<GroupMemberMapper, CzGroupMember> implements GroupMemberService {

    private final StudyGroupService studyGroupService;

    public GroupMemberServiceImpl(@Lazy StudyGroupService studyGroupService) {
        this.studyGroupService = studyGroupService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void joinGroup(String groupId, String userId) {
        // 1. 校验小组是否存在
        CzStudyGroup group = studyGroupService.getOne(new LambdaQueryWrapper<CzStudyGroup>().eq(CzStudyGroup::getId, groupId));
        if (group == null || group.getDeletedAt() != null) {
            throw new UserBadRequestException("小组不存在或已解散");
        }
        // 2. 校验小组状态是否正常
        if (group.getStatus() != null && group.getStatus() == 0) {
            throw new UserBadRequestException("该小组已被禁用，无法加入");
        }
        // 3. 检查小组人数是否已满
        long currentMemberCount = this.count(new LambdaQueryWrapper<CzGroupMember>()
                .eq(CzGroupMember::getGroupId, groupId));

        if (group.getMaxMembers() != null && currentMemberCount >= group.getMaxMembers()) {
            throw new UserConflictException("该小组已满员，无法加入");
        }
        CzGroupMember member = CzGroupMember.builder()
                .groupId(groupId)
                .userId(userId)
                .role(GroupMemberRole.MEMBER)
                .build();
        save(member);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void quitGroup(String groupId, String userId) {
        // 1. 查询成员信息
        LambdaQueryWrapper<CzGroupMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CzGroupMember::getGroupId, groupId)
                .eq(CzGroupMember::getUserId, userId);
        CzGroupMember member = this.getOne(wrapper);

        if (member == null) {
            throw new UserBadRequestException("您不是该小组成员");
        }

        // 2. 所有者不能直接退出，必须先转让或解散
        if (member.getRole() == GroupMemberRole.OWNER) {
            throw new UserBadRequestException("所有者不能直接退出小组，请先转让所有权或解散小组");
        }

        // 3. 执行退出
        boolean removed = this.remove(wrapper);
        if (!removed) {
            log.error("退出小组失败, groupId: {}, userId: {}", groupId, userId);
            throw new UserBadRequestException("退出小组失败");
        }

        log.info("用户 {} 退出小组 {}", userId, groupId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeMember(String groupId, String userId, String currentUserId) {
        CzGroupMember currentUser = isGroupMember(groupId, currentUserId);

        // 2. 检查当前用户是否有权限移除成员
        if (!currentUser.getRole().canRemoveMember()) {
            throw new com.cuizhi.core.exception.user.UserForbiddenException("您没有权限移除成员，需要管理员或所有者角色");
        }

        // 3. 查询目标用户
        LambdaQueryWrapper<CzGroupMember> targetUserWrapper = new LambdaQueryWrapper<>();
        targetUserWrapper.eq(CzGroupMember::getGroupId, groupId)
                .eq(CzGroupMember::getUserId, userId);
        CzGroupMember targetUser = this.getOne(targetUserWrapper);

        if (targetUser == null) {
            throw new UserBadRequestException("目标用户不是该小组成员");
        }

        // 4. 不能移除所有者
        if (targetUser.getRole() == GroupMemberRole.OWNER) {
            throw new UserBadRequestException("不能移除所有者");
        }

        // 5. 管理员不能移除其他管理员（只有所有者可以）
        if (targetUser.getRole() == GroupMemberRole.ADMIN &&
                currentUser.getRole() != GroupMemberRole.OWNER) {
            throw new com.cuizhi.core.exception.user.UserForbiddenException("只有所有者可以移除管理员");
        }

        // 6. 执行移除
        boolean removed = this.remove(targetUserWrapper);
        if (!removed) {
            log.error("移除成员失败, groupId: {}, userId: {}, operator: {}", groupId, userId, currentUserId);
            throw new UserBadRequestException("移除成员失败");
        }

        log.info("用户 {} 被用户 {} 从小组 {} 中移除", userId, currentUserId, groupId);
    }

    @Override
    public List<CzGroupMember> listMembers(String groupId) {
        // 1. 校验小组是否存在
        CzStudyGroup group = studyGroupService.getById(groupId);
        if (group == null) {
            throw new UserBadRequestException("学习小组不存在");
        }

        // 2. 查询成员列表
        LambdaQueryWrapper<CzGroupMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CzGroupMember::getGroupId, groupId);
        return this.list(wrapper);
    }

    private CzGroupMember isGroupMember(String groupId, String currentUserId) {
        // 1. 检查当前用户是否是小组成员
        LambdaQueryWrapper<CzGroupMember> currentUserWrapper = new LambdaQueryWrapper<>();
        currentUserWrapper.eq(CzGroupMember::getGroupId, groupId)
                .eq(CzGroupMember::getUserId, currentUserId);
        CzGroupMember currentUser = this.getOne(currentUserWrapper);

        if (currentUser == null) {
            throw new UserBadRequestException("您不是该小组成员");
        }
        return currentUser;
    }
}
