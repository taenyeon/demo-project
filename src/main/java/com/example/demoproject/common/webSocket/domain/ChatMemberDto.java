package com.example.demoproject.common.webSocket.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMemberDto {
    private long userSeq;
    private String userId;
    private String roomId;
}
