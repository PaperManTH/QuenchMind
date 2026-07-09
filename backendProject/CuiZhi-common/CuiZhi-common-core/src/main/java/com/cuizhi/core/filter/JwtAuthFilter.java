package com.cuizhi.core.filter;

import com.cuizhi.core.model.context.UserContext;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @Author thpaperman
 * @Description 通用 JWT 解析 Filter — 从 Authorization header 提取用户信息，写入 UserContext
 *              不依赖 Spring Security，所有模块共享。配置 jwt.secret 即生效。
 * @Date 2026/7/7
 * @Version 1.0
 */
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final SecretKey signingKey;

    public JwtAuthFilter(String secret) {
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String header = request.getHeader("Authorization");
            if (header != null && header.startsWith("Bearer ")) {
                String token = header.substring(7);
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(signingKey)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();
                String userId = claims.get("userId", String.class);
                String userName = claims.getSubject();
                if (userId != null) {
                    UserContext.set(new UserContext.UserInfo(userId, userName));
                }
            }
        } catch (Exception e) {
            log.debug("JWT 解析失败: {}", e.getMessage());
        }
        try {
            filterChain.doFilter(request, response);
        } finally {
            UserContext.clear();
        }
    }
}
