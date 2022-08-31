package com.example.demoproject.controller;

import com.example.demoproject.common.security.JwtTokenProvider;
import com.example.demoproject.common.security.entity.UserDetailCustom;
import com.example.demoproject.domain.Token;
import com.example.demoproject.domain.UserDto;
import com.example.demoproject.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import static com.example.demoproject.common.security.JwtAuthenticationProvider.COOKIE_NAME;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @PostMapping(value = "/login/process")
    @ResponseBody
    public ResponseEntity<?> tryLogin(Token.Request token, HttpServletResponse response) {
        log.info("login process");
        UserDto user = userService.findByUserIdWhenLogin(token.getId());
        if (!passwordEncoder.matches(token.getPwd(), user.getPwd())) {
            log.info("비밀번호 오류 id : {}", token.getId());
            throw new IllegalStateException("비밀번호 오류입니다.");
        }
        String generateToken = JwtTokenProvider.generateToken(new UserDetailCustom(user));
        ResponseCookie responseCookie = ResponseCookie.from(COOKIE_NAME, generateToken)
                .path("/")
                .secure(true)
                .sameSite("None")
                .httpOnly(true)
                .build();
        response.setHeader("Set-Cookie", responseCookie.toString());
        Token.Response responseToken = Token.Response.builder()
                .resultCode(200)
                .resultMessage("success")
                .token(generateToken)
                .build();
        return ResponseEntity.ok().body(responseToken);
    }

    @GetMapping("/logout")
    public String tryLogout(HttpServletRequest request,HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null){
            new SecurityContextLogoutHandler().logout(request,response,authentication);
        }
        else {
            throw new IllegalStateException("로그인 정보가 없습니다.");
        }
        ResponseCookie responseCookie = ResponseCookie.from(COOKIE_NAME, null)
                .path("/")
                .maxAge(0)
                .build();
        response.setHeader("Set-Cookie", responseCookie.toString());
        return "login";
    }

    @GetMapping("/regist")
    public String getRegistPage() {
        return "regist";
    }

    @PostMapping("/regist")
    public String insertUser(@Validated UserDto userDto) {
        log.info("registUser");
        userService.insertUser(userDto);
        return "test";
    }

    @ResponseBody
    @GetMapping("/{userSeq}")
    public UserDto findById(@PathVariable Long userSeq) {
        return userService.findById(userSeq);
    }

    @ResponseBody
    @GetMapping("/{userSeq}/delete")
    public String deleteUser(@PathVariable Long userSeq) {
        int i = userService.deleteUser(userSeq);
        if (i > 0) {
            return "success";
        }
        return "fail";
    }
}
