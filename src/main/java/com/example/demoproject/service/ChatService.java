package com.example.demoproject.service;

import com.example.demoproject.common.webSocket.domain.ChatRoomDto;
import com.example.demoproject.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {
    private final ChatRepository chatRepository;

    public List<ChatRoomDto> findAll() {
        return chatRepository.findAll();
    }

    public ChatRoomDto findById(String roomId) {
        return chatRepository.findById(roomId)
                .orElseThrow(() -> new IllegalStateException("roomId에 해당하는 채팅방을 찾을 수 없습니다."));
    }

    public int insertRoom(ChatRoomDto chatRoomDto) {
        log.info("roomDto : {}", chatRoomDto);
        return chatRepository.insert(chatRoomDto);
    }

    public ChatRoomDto joinById(String roomId) {
        return chatRepository.findById(roomId)
                .orElseThrow(() -> new IllegalStateException("roomId에 해당하는 채팅방을 찾을 수 없습니다."));
    }
}
