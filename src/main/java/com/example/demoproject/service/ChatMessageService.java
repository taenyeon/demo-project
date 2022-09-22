package com.example.demoproject.service;

import com.example.demoproject.common.webSocket.domain.ChatMessageDto;
import com.example.demoproject.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;

    public void insertMessage(ChatMessageDto chatMessageDto){
        chatMessageRepository.insertMessage(chatMessageDto);
    }

    public List<ChatMessageDto> findByRoomId(String roomId){
        return chatMessageRepository.findByRoomId(roomId);
    }
}
