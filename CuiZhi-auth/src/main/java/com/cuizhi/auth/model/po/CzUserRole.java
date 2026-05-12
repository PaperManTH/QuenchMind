package com.cuizhi.auth.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("cz_auth_user_role")
public class CzUserRole implements Serializable {

    private Long userId;
    private Long roleId;
    private LocalDateTime createdAt;
}

