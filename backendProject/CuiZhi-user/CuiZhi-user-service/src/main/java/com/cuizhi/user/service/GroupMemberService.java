package com.cuizhi.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cuizhi.user.model.po.CzGroupMember;

import java.util.List;

/**
 * @Author: thpaperman
 * @Date: 2026/5/13
 * @Description: 小组成员表 服务类
 * @Version: 1.0
 */
public interface GroupMemberService extends IService<CzGroupMember> {

    /**
     * 加入小组
     * @param groupId 小组ID
     * @param userId 用户ID
     */
    void joinGroup(String groupId, String userId);

    /**
     * 退出小组
     * @param groupId 小组ID
     * @param userId 用户ID
     */
    void quitGroup(String groupId, String userId);

    /**
     * 移除小组成员
     * @param groupId 小组ID
     * @param userId 用户ID
     * @param currentUserId 当前用户ID
     */
    void removeMember(String groupId, String userId, String currentUserId);

    /**
     * 获取小组成员列表
     * @param groupId 小组ID
     * @return 小组成员列表
     */
    List<CzGroupMember> listMembers(String groupId);
}
