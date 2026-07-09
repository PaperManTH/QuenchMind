package com.cuizhi.auth.utils;

import cn.hutool.core.lang.UUID;
import com.cuizhi.auth.model.dto.AuthResultDto;
import com.cuizhi.core.exception.auth.AuthCheckException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.cuizhi.core.common.RedisConstant.ACCESS_TOKEN_EXPIRATION;
import static com.cuizhi.core.common.RedisConstant.REFRESH_TOKEN_EXPIRATION;

/**
 * @Author thpaperman
 * @Description JWT 工具类
 * @Date 2026/4/6
 * @Version 1.0
 */
@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String SECRET;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    /**
     * 生成 Access Token 和 Refresh Token
     */
    public AuthResultDto generateTokens(String username, String userId) {
        String accessToken = generateAccessToken(username, userId);
        String refreshToken = generateRefreshToken(username, userId);

        return AuthResultDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(ACCESS_TOKEN_EXPIRATION / 1000)
                .refreshExpiresIn(REFRESH_TOKEN_EXPIRATION / 1000)
                .build();
    }

    /**
     * 生成 Access Token
     */
    public String generateAccessToken(String username, String userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("type", "access");

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuer("cuizhi-auth")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 生成 Refresh Token
     */
    public String generateRefreshToken(String username, String userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("type", "refresh");
        claims.put("jti", UUID.randomUUID().toString());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuer("cuizhi-auth")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 刷新 Token
     */
    public AuthResultDto refreshToken(String refreshToken) {
        try {
            var claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(refreshToken)
                    .getBody();

            String type = (String) claims.get("type");
            if (!"refresh".equals(type)) {
                throw new AuthCheckException("无效的 Token 类型");
            }

            String username = claims.getSubject();
            String userId = claims.get("userId").toString();

            return generateTokens(username, userId);
        } catch (Exception e) {
            throw new AuthCheckException("Refresh Token 无效或已过期");
        }
    }

    public String getUserNameFromToken(String token) {
        try {
            var claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // Subject 字段存储的是用户名
            return claims.getSubject();
        } catch (Exception e) {
            throw new AuthCheckException("用户信息异常");
        }
    }

    public String getUserIdFromToken(String token) {
        try {
            var claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.get("userId").toString();
        } catch (Exception e) {
            throw new AuthCheckException("用户信息异常");
        }
    }
}
