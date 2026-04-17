package com.cuizhi.auth.model.dto;

import com.cuizhi.auth.model.po.CzUser;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author thpaperman
 * @Description 用户扩展信息
 * @Date 2026/4/6
 * @Version 1.0
 */
@Data
public class CzUserExt extends CzUser {
    //用户权限
    List<String> permissions = new ArrayList<>();
}
