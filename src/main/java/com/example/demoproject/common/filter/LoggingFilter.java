package com.example.demoproject.common.filter;

import com.example.demoproject.common.security.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;
import org.thymeleaf.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static com.example.demoproject.common.security.JwtAuthenticationProvider.JWT_COOKIE_NAME;

@Slf4j
public class LoggingFilter implements Filter {

    private List<String> excludeList(){
        return List.of(
                "application/javascript",
                "text/html");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
            // log 조회 시, traceId를 통해 request에 해당하는 로그를 찾기 편함.
            MDC.put("traceId", UUID.randomUUID().toString());
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            String userSeq = " - ";
            if (httpRequest.getCookies()!= null){

            Cookie jwtCookie = Arrays.stream(httpRequest.getCookies())
                    .filter(cookie -> StringUtils.equalsIgnoreCase(cookie.getName(), JWT_COOKIE_NAME))
                    .findFirst()
                    .orElse(null);
            userSeq = jwtCookie != null ? JwtTokenProvider.getUserSeqFromJWT(jwtCookie.getValue()) : " - ";
            }
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            long startTime = System.currentTimeMillis();
            // 케싱을 통해 request,response의 데이터 보존
            ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(httpRequest);
            ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(httpResponse);
            Map<String, Object> requestHeaders = getRequestHeaders(requestWrapper);
            // 필터 체인 전, request logging
            log.info("[REQUEST] userSeq = {}, URL = {}, method = {}, headers = {}, param = {}, time = {}",
                    userSeq,
                    requestWrapper.getRequestURI(),
                    requestWrapper.getMethod(),
                    getRequestHeaders(requestWrapper),
                    getRequestBody(requestWrapper),
                    startTime
            );
            chain.doFilter(requestWrapper, responseWrapper); // controller
            // 필터 체인 후, response logging
            log.info("[RESPONSE] userSeq = {}, headers = {}, status = {}, body = {}, elapsedTime = {}",
                    userSeq,
                    getResponseHeaders(responseWrapper),
                    responseWrapper.getStatus(),
                    getResponseBody(responseWrapper),
                    System.currentTimeMillis() - startTime
            );
            MDC.clear();
        }
    }

    private Map<String, Object> getRequestHeaders(HttpServletRequest request) {
        Map<String, Object> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, request.getHeader(headerName));
        }
        return headers;
    }

    private Map<String, Object> getRequestBody(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<>();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            params.put(paramName, request.getParameter(paramName));
        }
        return params;
    }

    private Map<String, Object> getResponseHeaders(HttpServletResponse response) {
        Map<String, Object> headers = new HashMap<>();
        Collection<String> headerNames = response.getHeaderNames();
        headerNames.forEach(headerName -> {
            headers.put(headerName, response.getHeader(headerName));
        });
        headers.put("Content-Type",response.getContentType());
        return headers;
    }

    private String getResponseBody(HttpServletResponse response) throws IOException {
        String payload = null;
        ContentCachingResponseWrapper w = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        if (w != null) {
            byte[] buf = w.getContentAsByteArray();
            if (buf.length > 0) {
                payload = new String(buf, 0, buf.length, w.getCharacterEncoding());
                // response는 한번 읽을 경우, 보존이 안되기 때문에
                // response를 copy하여 저장.
                w.copyBodyToResponse();
            }
        }
        if (payload != null) {
            if (response.getContentType() != null) {
                boolean match = excludeList()
                        .stream()
                        .anyMatch(a -> response.getContentType().contains(a));
                if (match){
                return response.getContentType();
                }
            }
            if (payload.length() > 200) {
                payload = payload.substring(0, 200);
                payload += "Skip...";
            }
        } else {
            return "-";
        }
        return payload;
    }
}
