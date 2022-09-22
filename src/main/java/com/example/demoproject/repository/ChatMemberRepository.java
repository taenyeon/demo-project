package com.example.demoproject.repository;

import com.example.demoproject.common.webSocket.domain.ChatMemberDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;


@Mapper
public interface ChatMemberRepository {
    int insertMember(ChatMemberDto chatMemberDto);
    int deleteMember(long userSeq);
    Optional<ChatMemberDto> findBySeq(long userSeq);
    List<ChatMemberDto> findAllByRoomId(String roomId);
}
