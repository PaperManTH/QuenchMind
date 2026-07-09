package com.cuizhi.resource.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author thpaperman
 * @Description 上传结果
 * @Date 2026/7/7
 * @Version 1.0
 */
@Data
@AllArgsConstructor
public class UploadResult {
    private String resourceId;
    private String fileName;
    private boolean instant; // 是否秒传
}
