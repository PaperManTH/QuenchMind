package com.cuizhi.resource.service;

/**
 * @Author thpaperman
 * @Description 资源状态管理 — Python 处理完回调时更新
 * @Date 2026/7/7
 * @Version 1.0
 */
public interface ResourceStatusService {

    /** Python 回调：更新解析状态 + 摘要 */
    void updateParseResult(String resourceId, String summary, int chunkCount, String errorMsg);

    /** Python 回调：更新向量化状态 */
    void updateVectorStatus(String resourceId, int status);
}
