package com.cuizhi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.cuizhi.auth.mapper")
@SpringBootApplication
public class AuthControllerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthControllerApplication.class, args);
    }

}
