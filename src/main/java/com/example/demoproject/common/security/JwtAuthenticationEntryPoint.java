package com.example.demoproject.common.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        log.error("unauthorized error. Message - {}",authException.getMessage());
        Map<String,String> errorCode = (Map<String, String>) request.getAttribute("unauthorization.code");
        String error = errorCode.keySet().stream().findFirst().orElse(null);
        request.setAttribute("response.failure.code", error);
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, errorCode.get(error));
    }
}
