package com.example.demoproject.repository;

import com.example.demoproject.common.webSocket.domain.ChatMessageDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChatMessageRepository {
    int insertMessage(ChatMessageDto chatMessageDto);
    List<ChatMessageDto> findByRoomId(String roomId);
    int deleteMessage(long messageSeq);
}
