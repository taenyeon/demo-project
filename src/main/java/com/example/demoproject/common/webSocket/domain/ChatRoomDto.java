package com.example.demoproject.common.webSocket.domain;

import com.example.demoproject.common.security.entity.UserDetailCustom;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.web.socket.WebSocketSession;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomDto {
    private String roomId;
    private String name;
    private Long createBySeq;
    private String createByUserId;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime registDateTime;
    private List<ChatMemberDto> chatMemberDtos;
    private List<ChatMessageDto> chatMessageDtos;
    private Set<WebSocketSession> sessions = new HashSet<>();

    public static ChatRoomDto create(String name, UserDetailCustom userDetailCustom){
        return ChatRoomDto.builder()
                .roomId(UUID.randomUUID().toString())
                .name(name)
                .createBySeq(userDetailCustom.getUserSeq())
                .createByUserId(userDetailCustom.getId())
                .registDateTime(LocalDateTime.now())
                .build();
    }
}
