package com.example.demoproject.common.security;

import com.example.demoproject.common.security.entity.UserAuthentication;
import com.example.demoproject.common.security.entity.UserDetailCustom;
import com.example.demoproject.domain.RefreshTokenDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.thymeleaf.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.HttpCookie;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;

import static com.example.demoproject.common.security.JwtTokenProvider.REFRESH_TOKEN_KEY;

@Slf4j
@Component
@AllArgsConstructor
public class JwtAuthenticationProvider extends OncePerRequestFilter {
    // jwt 토큰을 통해 인증 조회
    public static final String JWT_COOKIE_NAME = "jwt";
    private final CustomUserDetailService customUserDetailService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);
            String userSeq = null;
            if (!StringUtils.isEmptyOrWhitespace(jwt)) {
                if (JwtTokenProvider.validateToken(jwt)) {
                    userSeq = JwtTokenProvider.getUserSeqFromJWT(jwt);
                    // JWT 토큰 유효성 검사 (유효시간)
                    // Redis를 사용하여 cache로 구현함.
                    setUserDetail(userSeq);
                } else {
                    request.setAttribute("unauthorization", "401-001 인증키 만료.");
                    // 유효시간 만료시, 재발급.
                    String key = REFRESH_TOKEN_KEY + userSeq;
                    RefreshTokenDto refreshTokenDto = (RefreshTokenDto) redisTemplate.opsForValue().get(key);
                    if (refreshTokenDto != null) {
                        String accessToken;
                        Date now = new Date();
                        long diff = now.getTime() - refreshTokenDto.getExpiredAt().getTime() / 1000 / (24 * 60 * 60);
                        if (diff < 1) {
                            // refreshToken의 만료시간을 체크하여, 만료전 재발급.
                            accessToken = jwtTokenProvider.generateToken(setUserDetail(userSeq));
                        } else {
                            // 아직 만료시간이 남았을 경우, AccessToken만 재발급.
                            accessToken = jwtTokenProvider.generateAccessToken(setUserDetail(userSeq));
                        }
                        ResponseCookie responseCookie = ResponseCookie.from(JWT_COOKIE_NAME, accessToken)
                                .path("/")
                                .secure(true)
                                .sameSite("None")
                                .httpOnly(true)
                                .build();
                        response.setHeader("Set-Cookie", responseCookie.toString());
                    } else {
                        // todo refreshToken이 없을 경우, 기존의 쿠키 제거 & 다시 로그인하도록 설정 필요.
                        request.setAttribute("unauthorization", "refresh token 없음.");
                    }
                }
            } else {
                request.setAttribute("unauthorization", "401 인증키 없음.");
            }
        } catch (Exception e) {
                response.setHeader("Set-Cookie", null);
                log.error("errorMessage", e);
        }
        filterChain.doFilter(request, response);
    }

    private UserDetailCustom setUserDetail(String userSeq) {
        UserDetailCustom user = customUserDetailService.loadUserByUserSeq(Long.parseLong(userSeq));
        UserAuthentication userAuthentication = new UserAuthentication(user, null, user.getAuthorities());
        userAuthentication.setDetails(user);
        SecurityContextHolder.getContext().setAuthentication(userAuthentication);
        return user;
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            return Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals(JWT_COOKIE_NAME))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElse(null);
        } else {
            return null;
        }
    }
}
