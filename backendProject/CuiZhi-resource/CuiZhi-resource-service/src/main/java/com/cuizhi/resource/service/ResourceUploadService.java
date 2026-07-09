package com.cuizhi.resource.service;

import com.cuizhi.resource.model.dto.UploadResult;

import java.util.Set;

/**
 * @Author thpaperman
 * @Description 文件上传服务（分片上传 + 秒传 + 断点续传 + 合并）
 * @Date 2026/7/7
 * @Version 1.1
 */
public interface ResourceUploadService {

    /** 秒传检查，已上传返回 resourceId */
    String checkByMd5(String fileMd5);

    /** 上传单个分片 */
    void uploadChunk(String fileMd5, String ext, int chunkNo, int totalChunks, byte[] chunkData);

    /** 查询已上传的分片序号（断点续传） */
    Set<Integer> getUploadedChunks(String fileMd5);

    /** 合并分片 */
    UploadResult mergeChunks(String fileMd5, String fileName, String userId);
}
