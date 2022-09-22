package com.example.demoproject.repository;

import com.example.demoproject.common.webSocket.domain.ChatRoomDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Mapper
public interface ChatRepository {
    List<ChatRoomDto> findAll();
    Optional<ChatRoomDto> findById(String roomId);
    int insert(ChatRoomDto chatRoomDto);
    Optional<ChatRoomDto> joinById(String roomId);
}
