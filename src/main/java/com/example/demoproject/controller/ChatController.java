package com.example.demoproject.controller;

import com.example.demoproject.common.security.entity.UserDetailCustom;
import com.example.demoproject.common.webSocket.domain.ChatRoomDto;
import com.example.demoproject.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/chat")
@RequiredArgsConstructor
@Slf4j
public class ChatController {
    private final ChatService chatService;

    @GetMapping("")
    public String getRooms(Model model){
        List<ChatRoomDto> all = chatService.findAll();
        model.addAttribute("rooms",all);
        return "/chat";
    }

    @GetMapping("/{roomId}")
    public String getRoom(Model model, @PathVariable String roomId, @CookieValue("jwt") String jwt){
        model.addAttribute("room",chatService.findById(roomId));
        model.addAttribute("jwt",jwt);
        return "/chatRoom";
    }
    @PostMapping("")
    public String createRoom(String name, @AuthenticationPrincipal UserDetailCustom userDetailCustom){
        ChatRoomDto chatRoomDto = ChatRoomDto.create(name, userDetailCustom.getUserSeq());
        log.info("Create ChatRoom - userSeq : {}, chatRoomDto : {}",userDetailCustom.getUserSeq(), chatRoomDto);
        int result = chatService.insertRoom(chatRoomDto);
        return "redirect:/chat";
    }
}
