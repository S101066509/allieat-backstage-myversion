package com.allieat.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    //準備發出去的秘鑰，寫在application.properties
    private final Key key;
    @Autowired
    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }
    public String generateToken(String username) {
        Integer setTime = 30*60*1000;
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() +  setTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    //驗證token
    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    //解析JWT
    private Claims getClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    //提取token創造時間
    public Date getCreationTime(String token) {
        Claims claims = getClaims(token); // 解析 Token
        return claims.getIssuedAt();  // 回傳創建時間
    }
}
