package com.cuizhi.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cuizhi.user.model.dto.StudyGroupDTO;
import com.cuizhi.user.model.po.CzStudyGroup;
import com.cuizhi.user.model.vo.GroupDetailsVO;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

/**
 * @Author: thpaperman
 * @Date: 2026/5/13
 * @Description: 学习小组表 服务类
 * @Version: 1.0
 */
public interface StudyGroupService extends IService<CzStudyGroup> {

    /**
     * 创建学习小组
     * @param studyGroup 学习小组信息
     * @param userId 创建人Id
     */
    void createGroup(StudyGroupDTO studyGroup, String userId);

    /**
     * 获取当前用户所属学习小组信息列表
     * @param userId 用户Id
     * @return 学习小组信息列表
     */
    List<CzStudyGroup> listGroups(String userId);

    /**
     * 获取学习小组详情
     *
     * @param groupId 学习小组Id
     * @param userId
     * @return 学习小组详情
     */
    GroupDetailsVO getGroupDetail(@NotBlank String groupId, String userId);

    /**
     * 修改学习小组信息
     *
     * @param studyGroup 学习小组信息
     * @param userId     修改人Id
     * @return 修改后的学习小组信息
     */
    CzStudyGroup updateGroup(StudyGroupDTO studyGroup, String userId);

    /**
     * 删除学习小组
     * @param groupId 学习小组Id
     * @param userId 删除人Id
     */
    void disbandGroup(@NotBlank String groupId, String userId);
}
