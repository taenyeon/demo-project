package com.example.demoproject.common.webSocket.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.web.socket.WebSocketSession;

import java.time.LocalDateTime;
import java.util.HashSet;
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
    private Long createBy;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime registDateTime;
    private Set<WebSocketSession> sessions = new HashSet<>();

    public static ChatRoomDto create(String name, long createBy){
        return ChatRoomDto.builder()
                .roomId(UUID.randomUUID().toString())
                .name(name)
                .createBy(createBy)
                .registDateTime(LocalDateTime.now())
                .build();
    }
}
