package com.example.demoproject.controller;

import com.example.demoproject.common.security.entity.UserDetailCustom;
import com.example.demoproject.common.webSocket.domain.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatMessageController {
    private final SimpMessagingTemplate template;

    @MessageMapping(value = "/chat/join")
    public void joinRoom(ChatMessageDto messageDto, @AuthenticationPrincipal UserDetailCustom userDetailCustom) {
        messageDto.setMessage(userDetailCustom.getName() + "님이 채팅방에 참여하였습니다.");
        template.convertAndSend("/sub/chat/room/"+messageDto.getRoomId(),messageDto);
    }

    @MessageMapping(value = "/chat/message")
    public void message(ChatMessageDto message){
        template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }
}
