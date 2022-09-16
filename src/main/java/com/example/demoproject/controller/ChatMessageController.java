package com.example.demoproject.controller;

import com.example.demoproject.common.security.CustomUserDetailService;
import com.example.demoproject.common.security.JwtTokenProvider;
import com.example.demoproject.common.security.entity.UserDetailCustom;
import com.example.demoproject.common.webSocket.domain.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatMessageController {
    private final SimpMessagingTemplate template;
    private final CustomUserDetailService customUserDetailService;

    @MessageMapping(value = "/chat/join")
    public void joinRoom(ChatMessageDto messageDto, @Header("jwt") String jwt) {
        String userSeq = JwtTokenProvider.getUserSeqFromJWT(jwt);
        UserDetailCustom userDetailCustom = customUserDetailService.loadUserByUserSeq(Long.parseLong(userSeq));
        messageDto.setWriter("SYSTEM");
        messageDto.setMessage(userDetailCustom.getId()+ "님이 채팅방에 참여하였습니다.");
        template.convertAndSend("/sub/chat/room/"+messageDto.getRoomId(),messageDto);
    }

    @MessageMapping(value = "/chat/message")
    public void message(ChatMessageDto message){
        template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }
}
