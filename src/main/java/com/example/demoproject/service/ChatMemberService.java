package com.example.demoproject.service;

import com.example.demoproject.common.webSocket.domain.ChatMemberDto;
import com.example.demoproject.repository.ChatMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatMemberService {
    private final ChatMemberRepository chatMemberRepository;

    public void insertMember(ChatMemberDto chatMemberDto) {
        chatMemberRepository.insertMember(chatMemberDto);
    }

    public void deleteMember(long userSeq) {
        chatMemberRepository.deleteMember(userSeq);
    }

    public List<ChatMemberDto> findByRoomId(String roomId){
        return chatMemberRepository.findAllByRoomId(roomId);
    }

}
