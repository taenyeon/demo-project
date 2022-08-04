package com.example.demoproject.common.security;

import com.example.demoproject.common.security.entity.UserAuthentication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.thymeleaf.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
public class JwtAuthenticationProvider extends OncePerRequestFilter {
    public static final String COOKIE_NAME = "jwt";
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);
            log.info("jwt : {}",jwt);
            if (!StringUtils.isEmptyOrWhitespace(jwt) && JwtTokenProvider.validateToken(jwt)) {
                String id = JwtTokenProvider.getUserIdFromHJWT(jwt);
                log.info("id : {}",id);
                UserAuthentication userAuthentication = new UserAuthentication(id, null, null);
                userAuthentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(userAuthentication);
            }else {
                if (StringUtils.isEmpty(jwt)){
                    request.setAttribute("unauthorization", "401 인증키 없음.");
                } else {
                    request.setAttribute("unauthorization","401-001 인증키 만료.");
                }
                log.info("Error request : {}",request.getAttribute("unauthorization"));
            }
        } catch (Exception e) {
            log.error("errorMessage",e);
        }
        filterChain.doFilter(request,response);
    }

    private String getJwtFromRequest(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            Optional<Cookie> jwt = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals(COOKIE_NAME)).findFirst();
            return jwt.map(Cookie::getValue).orElse(null);
        }else {
            return null;
        }
//        String bearerToken = request.getHeader("Authorization");
//        if (!StringUtils.isEmptyOrWhitespace(bearerToken) && bearerToken.startsWith("Bearer ")){
//            return bearerToken.substring("Bearer".length());
//        }
    }
}
