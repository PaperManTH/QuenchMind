package com.cuizhi.auth.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author thpaperman
 * @Description MyBatis-Plus 配置类
 * @Date 2026/4/6
 * @Version 1.0
 */
@Configuration
@MapperScan("com.cuizhi.auth.mapper")
public class MybatisPlusConfig {

	/**
	 * 新的分页插件
	 * 需要设置 MybatisConfiguration#useDeprecatedExecutor = false
	 * 避免缓存出现问题
	 */
	@Bean
	public MybatisPlusInterceptor mybatisPlusInterceptor() {
		MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
		interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
		return interceptor;
	}


}