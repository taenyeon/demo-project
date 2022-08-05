package com.example.demoproject.common.config;

import com.example.demoproject.common.filter.LoggingFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.support.ErrorPageFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public ErrorPageFilter errorPageFilter() {
        return new ErrorPageFilter();
    }

    @Bean
    public FilterRegistrationBean getFilterRegistrationBean() {
        // logging filter 적용
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(new LoggingFilter());
        registrationBean.setOrder(Integer.MIN_VALUE);
        return registrationBean;

    }

    @Bean
    public FilterRegistrationBean disableErrorPageFilter(ErrorPageFilter errorPageFilter) {
        // errorPage filter 사용 X
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(errorPageFilter);
        registrationBean.setEnabled(false);
        return registrationBean;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // spring security 기본 사용 password Encoder를 빈으로 등록하여
        // DB에서 가져온 pwd 조회
        return new BCryptPasswordEncoder();
    }

}
