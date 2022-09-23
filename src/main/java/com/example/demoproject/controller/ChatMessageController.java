package com.example.demoproject.controller;

import com.example.demoproject.common.security.CustomUserDetailService;
import com.example.demoproject.common.security.JwtTokenProvider;
import com.example.demoproject.common.security.entity.UserDetailCustom;
import com.example.demoproject.common.webSocket.domain.ChatMemberDto;
import com.example.demoproject.common.webSocket.domain.ChatMessageDto;
import com.example.demoproject.service.ChatMemberService;
import com.example.demoproject.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatMessageController {
    private final SimpMessagingTemplate template;
    private final CustomUserDetailService customUserDetailService;
    private final ChatMessageService chatMessageService;
    private final ChatMemberService chatMemberService;

    @MessageMapping(value = "/chat/join")
    public void joinRoom(ChatMessageDto messageDto, @Header("jwt") String jwt) {
        ChatMemberDto chatMemberDto = getChatMemberDto(jwt, messageDto);
        if (!chatMemberService.isRoomMember(chatMemberDto)) {
        messageDto.setWriter("SYSTEM");
        messageDto.setMessage(chatMemberDto.getUserId() + "님이 채팅방에 참여하였습니다.");
            chatMemberService.insertMember(chatMemberDto);
        template.convertAndSend("/sub/chat/room/" + messageDto.getRoomId(), messageDto);
        }
    }

    @MessageMapping(value = "/chat/message")
    public void message(ChatMessageDto message, @Header("jwt") String jwt) {
        ChatMemberDto chatMemberDto = getChatMemberDto(jwt, message);
        message.setWriterSeq(chatMemberDto.getUserSeq());
        log.info("send Message : {}",message);
        chatMessageService.insertMessage(message);
        template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }

    @MessageMapping(value = "/chat/out")
    public void outRoom(ChatMessageDto messageDto, @Header("jwt") String jwt) {
        ChatMemberDto chatMemberDto = getChatMemberDto(jwt, messageDto);
        messageDto.setWriter("SYSTEM");
        messageDto.setMessage(chatMemberDto.getUserId() + "님이 채팅방에서 나갔습니다.");
        if (!chatMemberService.isRoomMember(chatMemberDto)) {
            chatMemberService.deleteMember(chatMemberDto.getUserSeq());
        }
        template.convertAndSend("/sub/chat/room/" + messageDto.getRoomId(), messageDto);
    }

    @GetMapping("/chat/join/{roomId}")
    public ResponseEntity<?> getMessageWhenJoin(@PathVariable String roomId){
        List<ChatMessageDto> chatMessageDtos = chatMessageService.findByRoomId(roomId);
        log.info("chatMessageDtos : {}",chatMessageDtos);
        return ResponseEntity.ok().body(chatMessageDtos);
    }

    private UserDetailCustom getUserInfo(String jwt) {
        long userSeq = Long.parseLong(JwtTokenProvider.getUserSeqFromJWT(jwt));
        return customUserDetailService.loadUserByUserSeq(userSeq);
    }

    private ChatMemberDto getChatMemberDto(String jwt, ChatMessageDto messageDto) {
        UserDetailCustom userDetailCustom = getUserInfo(jwt);
        return ChatMemberDto.builder()
                .roomId(messageDto.getRoomId())
                .userId(messageDto.getWriter())
                .userSeq(userDetailCustom.getUserSeq())
                .build();
    }
}
