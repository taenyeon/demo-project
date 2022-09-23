package com.example.demoproject.common.webSocket.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatMessageDto {
    private long messageSeq;
    private String roomId;
    private long writerSeq;
    private String writer;
    private String message;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime registDateTime;
}
