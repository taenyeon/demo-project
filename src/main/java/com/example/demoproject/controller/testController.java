package com.example.demoproject.controller;

import com.example.demoproject.domain.TestDto;
import com.example.demoproject.common.security.entity.UserDetailCustom;
import com.example.demoproject.service.TestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/v1")
@Slf4j
@RequiredArgsConstructor
public class testController {
    private final TestService testService;

    @GetMapping("/test")
    public String test(Model model,
                       @AuthenticationPrincipal UserDetailCustom userDetailCustom) {
        log.info(userDetailCustom.getId());
        log.info(String.valueOf(userDetailCustom.getAuthorities()));
        return "test";
    }

    @ResponseBody
    @GetMapping("/test2")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String restTest(String value) {
        return "is Admin!";
    }

    @ResponseBody
    @GetMapping("/test3")
    public int test3(int value) {
        return testService.selectTestNum(value);
    }

    @ResponseBody
    @GetMapping("/test4")
    public List<TestDto> test4() {
        int i = 0;
        List<TestDto> testEntities = new ArrayList<>();
        while (i < 10) {
            TestDto test = TestDto.builder().
                    name(String.valueOf(i)).
                    value(String.valueOf(i)).
                    build();
            testEntities.add(test);
            i++;
        }
        return testEntities;
    }

    @GetMapping("/chat")
    public String getChatPage(){
        return "chat";
    }

}
