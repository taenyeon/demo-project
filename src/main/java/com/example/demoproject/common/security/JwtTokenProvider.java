package com.example.demoproject.common.security;

import com.example.demoproject.common.security.entity.UserDetailCustom;
import com.example.demoproject.domain.RefreshTokenDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@AllArgsConstructor
@Component
public class JwtTokenProvider {

    private static final String JWT_SECRET = "secretKey";
    private final long ACCESS_TOKEN_EXPIRATION_MS = Duration.ofMinutes(10).toMillis();
    private final long REFRESH_TOKEN_EXPIRATION_MS = Duration.ofDays(2).toMillis();
    public static final String REFRESH_TOKEN_KEY = "refreshToken:";

    private final RedisTemplate<String, Object> redisTemplate;

    public String generateToken(UserDetailCustom userDetailCustom) {
        generateRefreshToken(userDetailCustom);
        return generateAccessToken(userDetailCustom);
    }

    public String generateAccessToken(UserDetailCustom userDetailCustom) {
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + ACCESS_TOKEN_EXPIRATION_MS);
        return Jwts.builder()
                .setSubject(String.valueOf(userDetailCustom.getUserSeq()))
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }

    public void generateRefreshToken(UserDetailCustom userDetailCustom) {
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + REFRESH_TOKEN_EXPIRATION_MS);
        String refreshToken = Jwts.builder()
                .setSubject(String.valueOf(userDetailCustom.getUserSeq()))
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
        String key = REFRESH_TOKEN_KEY + userDetailCustom.getUserSeq();
        RefreshTokenDto refreshTokenDto = RefreshTokenDto.builder()
                .refreshToken(refreshToken)
                .creatAt(now)
                .expiredAt(expiredDate)
                .build();
        redisTemplate.opsForValue().set(key, refreshTokenDto);
        redisTemplate.expire(key, REFRESH_TOKEN_EXPIRATION_MS, TimeUnit.DAYS);
    }

    public static String getUserSeqFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public static boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(JWT_SECRET)
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.error("JWT Error - Message = {}", e.getMessage());
        }
        return false;
    }


}
