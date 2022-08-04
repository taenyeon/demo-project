package com.example.demoproject.controller;

import com.example.demoproject.common.security.JwtTokenProvider;
import com.example.demoproject.common.security.entity.UserAuthentication;
import com.example.demoproject.entity.Token;
import com.example.demoproject.entity.UserEntity;
import com.example.demoproject.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    public String getLoginPage(){
        return "login";
    }

    @PostMapping(value = "/login/process")
    @ResponseBody
    public ResponseEntity<?> tryLogin(Token.Request token, HttpServletResponse response){
        log.info("login process");
        UserEntity user = userService.findByIdWhenLogin(token.getId());
        if (!passwordEncoder.matches(token.getPwd(),user.getPwd())){
            log.info("비밀번호 오류 id : {}",token.getId());
            throw new IllegalStateException("비밀번호 오류입니다.");
        }
        Authentication authentication = new UserAuthentication(token.getId(),null,null);
        String responseToken = JwtTokenProvider.generateToken(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        ResponseCookie responseCookie = ResponseCookie.from(COOKIE_NAME,responseToken)
                .path("/")
                .secure(true)
                .sameSite("None")
                .httpOnly(true)
                .build();
        response.setHeader("Set-Cookie",responseCookie.toString());
        return ResponseEntity.ok().body(responseToken);
    }

    @GetMapping("/regist")
    public String getRegistPage(){
        return "regist";
    }

    @PostMapping("/regist")
    public String insertUser(UserEntity userEntity){
        log.info("registUser");
        int i = userService.insertUser(userEntity);
        return "test";
    }

    @ResponseBody
    @GetMapping("/{userSeq}")
    public UserEntity findById(@PathVariable Long userSeq){
       return userService.findById(userSeq);
    }

    @ResponseBody
    @GetMapping("/{userSeq}/delete")
    public String deleteUser(@PathVariable Long userSeq){
        int i = userService.deleteUser(userSeq);
        if (i>0){
            return "success";
        }
        return "fail";
    }
}
