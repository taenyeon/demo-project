package com.example.demoproject.common.config;

import com.example.demoproject.common.security.JwtAuthenticationEntryPoint;
import com.example.demoproject.common.security.JwtAuthenticationProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@Slf4j
public class SecurityConfig {
    @Autowired
    JwtAuthenticationProvider jwtAuthenticationProvider;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web -> web.ignoring().antMatchers(
                "/static/js/**",
                "/static/css/**",
                "/static/img/**"
        ));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf().disable()
                // session 사용 잠금
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .headers().frameOptions().sameOrigin()
                .and()
                // UsernamePasswordAuth 필터 앞(모든 security 필더 앞)에 커스텀 필터 적용
                .addFilterBefore(jwtAuthenticationProvider, UsernamePasswordAuthenticationFilter.class)
                // request 권한 설정
                .authorizeRequests()
                // 예외 url
                .antMatchers(
                        "/user/regist"
                        , "/user/login"
                        , "/user/login/process"
                        , "/js/**",
                        "/css/**",
                        "/img/**",
                        "/favicon.ico"
                )
                // 인증이 없어도 request 가능
                .permitAll()
                // 이외 모든 url
                .anyRequest()
                // 권한 필요
                .authenticated()
                .and()
                .exceptionHandling()
                // 인증 (회원 여부) 오류 핸들링 -> 없을 경우
                .authenticationEntryPoint(((request, response, authException) -> {
                    log.info("unauthorized error. Message - {}", authException.getMessage());
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.setContentType("text/html");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(getUnAuthMessage("로그인 후, 이용해주세요.", "/user/login"));
                }))
                // 인가 (권한 여부) 오류 핸들링 -> 없을 경우
                .accessDeniedHandler(((request, response, accessDeniedException) -> {
                    log.info("accessDenied error. Message - {}", accessDeniedException.getMessage());
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.setContentType("text/html");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(getUnAuthMessage("권한이 없습니다.", null));
                }))
                .and()
                // security 기본 제공 로그인 form 사용 X
                .formLogin().disable();
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        // cors 설정
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*");
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE"));
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    private String getUnAuthMessage(String message, String path) {
        // 에러 메세지
        return new StringBuilder()
                .append("<script>")
                .append("alert('")
                .append(message)
                .append("');")
                .append(path != null ? "location.href= '" + path + "'" : "history.go(-1)")
                .append(";")
                .append("</script>")
                .toString();

    }
}
