package com.cuizhi.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cuizhi.core.exception.user.UserBadRequestException;
import com.cuizhi.core.exception.user.UserForbiddenException;
import com.cuizhi.user.mapper.StudyGroupMapper;
import com.cuizhi.user.model.dto.StudyGroupDTO;
import com.cuizhi.user.model.enums.GroupMemberRole;
import com.cuizhi.user.model.po.CzGroupMember;
import com.cuizhi.user.model.po.CzStudyGroup;
import com.cuizhi.user.model.vo.GroupDetailsVO;
import com.cuizhi.user.service.GroupMemberService;
import com.cuizhi.user.service.StudyGroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 学习小组服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StudyGroupServiceImpl extends ServiceImpl<StudyGroupMapper, CzStudyGroup> implements StudyGroupService {

    private final GroupMemberService groupMemberService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createGroup(StudyGroupDTO studyGroup, String userId) {
        String groupId = UUID.fastUUID().toString();
        studyGroup.setGroupId(groupId);

        CzStudyGroup czStudyGroup = new CzStudyGroup();
        BeanUtil.copyProperties(studyGroup, czStudyGroup);
        czStudyGroup.setOwnerId(userId);
        save(czStudyGroup);

        // 创建者以 OWNER 身份加入
        groupMemberService.save(CzGroupMember.builder()
                .groupId(groupId)
                .userId(userId)
                .role(GroupMemberRole.OWNER)
                .build());
    }

    @Override
    public List<CzStudyGroup> listGroups(String userId) {
        LambdaQueryWrapper<CzStudyGroup> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CzStudyGroup::getOwnerId, userId);
        return list(queryWrapper);
    }

    @Override
    public GroupDetailsVO getGroupDetail(String groupId, String userId) {
        // 1. 查询小组信息
        LambdaQueryWrapper<CzStudyGroup> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CzStudyGroup::getId, groupId);
        CzStudyGroup czStudyGroup = getOne(queryWrapper);

        // 2. 查询小组成员信息
        LambdaQueryWrapper<CzGroupMember> memberQueryWrapper = new LambdaQueryWrapper<>();
        memberQueryWrapper.eq(CzGroupMember::getGroupId, groupId)
                .eq(CzGroupMember::getUserId, userId);
        CzGroupMember currentMember = groupMemberService.getOne(memberQueryWrapper);
        List<CzGroupMember> members = groupMemberService.listMembers(groupId);

        GroupDetailsVO detailsVO = GroupDetailsVO.builder()
                .members(members)
                .currentUserRole(currentMember.getRole().getCode())
                .isMember(true)
                .build();
        BeanUtil.copyProperties(czStudyGroup, detailsVO);

        return detailsVO;
    }

    @Override
    public CzStudyGroup updateGroup(StudyGroupDTO studyGroup, String userId) {
        LambdaQueryWrapper<CzGroupMember> memberQueryWrapper = new LambdaQueryWrapper<>();
        memberQueryWrapper.eq(CzGroupMember::getGroupId, studyGroup.getGroupId())
                .eq(CzGroupMember::getUserId, userId);
        CzGroupMember currentMember = groupMemberService.getOne(memberQueryWrapper);
        // 1. 校验用户权限
        if (!currentMember.getRole().canUpdateGroup()) {
            throw new UserForbiddenException("成员无权限修改小组信息");
        }
        CzStudyGroup czStudyGroup = new CzStudyGroup();
        BeanUtil.copyProperties(studyGroup, czStudyGroup);
        boolean update = updateById(czStudyGroup);
        if (!update) {
            log.error("更新小组信息失败, groupId: {}, userId: {}", studyGroup.getGroupId(), userId);
            throw new UserBadRequestException("更新小组信息失败");
        }
        return czStudyGroup;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disbandGroup(String groupId, String userId) {
        CzStudyGroup group = this.getById(groupId);
        if (group == null) {
            throw new UserBadRequestException("学习小组不存在");
        }
        if (!group.getOwnerId().equals(userId)) {
            throw new UserForbiddenException("非小组创建者无权解散小组");
        }
        // 删除小组成员
        groupMemberService.remove(new LambdaQueryWrapper<CzGroupMember>().eq(CzGroupMember::getGroupId, groupId));
        // 逻辑删除小组
        boolean removed = removeById(groupId);
        if (!removed) {
            log.error("解散小组失败, groupId: {}, userId: {}", groupId, userId);
            throw new UserBadRequestException("解散小组失败");
        }
    }
}