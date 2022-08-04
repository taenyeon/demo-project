package com.example.demoproject.common.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;

import java.util.Date;

@Slf4j
public class JwtTokenProvider {
    private static final String JWT_SECRET = "secretKey";

    private static final int JWT_EXPIRATION_MS = 604800000;

    public static String generateToken(Authentication authentication){
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + JWT_EXPIRATION_MS);
        return Jwts.builder()
                .setSubject(String.valueOf(authentication.getPrincipal()))
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS512,JWT_SECRET)
                .compact();
    }

    public static String getUserIdFromHJWT(String token){
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
