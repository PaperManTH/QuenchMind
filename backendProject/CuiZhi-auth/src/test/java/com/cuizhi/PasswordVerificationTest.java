package com.cuizhi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class PasswordVerificationTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void verifyPassword() {
        String storedHash = "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy";
        
        // 测试几个常见密码
        System.out.println("测试密码 '123456': " + passwordEncoder.matches("123456", storedHash));
        System.out.println("测试密码 'password': " + passwordEncoder.matches("password", storedHash));
        System.out.println("测试密码 'admin': " + passwordEncoder.matches("admin", storedHash));
        System.out.println("测试密码 'test': " + passwordEncoder.matches("test", storedHash));
        
        // 生成一个新的 123456 哈希
        String newHash = passwordEncoder.encode("123456");
        System.out.println("\n新生成的 123456 哈希: " + newHash);
        System.out.println("验证新哈希: " + passwordEncoder.matches("123456", newHash));
    }
}
