package com.example.demoproject.common.security;

import com.example.demoproject.common.security.entity.UserDetailCustom;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public class JwtTokenProvider {
    private static final String JWT_SECRET = "secretKey";

    private static final int JWT_EXPIRATION_MS = 604800000;

    public static String generateToken(UserDetailCustom userDetailCustom){
        log.info(userDetailCustom.getId());
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + JWT_EXPIRATION_MS);
        return Jwts.builder()
                .setSubject(String.valueOf(userDetailCustom.getUserSeq()))
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS512,JWT_SECRET)
                .compact();
    }

    public static String getUserSeqFromJWT(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public static boolean validateToken(String token){
        try{
            Jwts.parser()
                    .setSigningKey(JWT_SECRET)
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e){
            log.error("JWT Error - Message = {}",e.getMessage());
        }
        return false;
    }


}
