package com.cuizhi.feign;

import com.cuizhi.core.model.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: thpaperman
 * @Date: 2026/5/14
 * @Description: 资源服务Feign客户端
 * @Version: 1.0
 */
@FeignClient(name = "cuizhi-resource", path = "/resource") // name 对应 Nacos 中的服务名
public interface ResourceFeignClient {

    /**
     * 上传文件
     * @param file 文件
     * @return 文件URL
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseResult<String> uploadFile(@RequestPart("file") MultipartFile file, @RequestParam("path") String path);
}