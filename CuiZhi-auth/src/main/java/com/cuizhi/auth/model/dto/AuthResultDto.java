package com.cuizhi.auth.model.dto;

import com.cuizhi.auth.model.po.CzUser;
import lombok.Data;
import lombok.ToString;

/**
 * @Author thpaperman
 * @Description 认证结果信息
 * @Date 2026/4/15
 * @Version 1.0
 */
@Data
@ToString
public class AuthResultDto extends CzUser {

    /** JWT 令牌 **/
    private String token;

}
