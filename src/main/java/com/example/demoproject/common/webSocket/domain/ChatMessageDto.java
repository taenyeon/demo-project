package com.example.demoproject.common.webSocket.domain;

import lombok.Data;

@Data
public class ChatMessageDto {
    private long messageSeq;
    private String roomId;
    private long writerSeq;
    private String writer;
    private String message;
}
