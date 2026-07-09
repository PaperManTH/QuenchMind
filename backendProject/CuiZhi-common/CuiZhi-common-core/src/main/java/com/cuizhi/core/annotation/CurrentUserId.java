package com.cuizhi.core.annotation;

import java.lang.annotation.*;

/**
 * @Author: thpaperman
 * @Date: 2026/5/15
 * @Description: 自动注入当前用户Id
 * @Version: 1.0
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CurrentUserId {
}
