package com.example.demoproject.repository;

import com.example.demoproject.domain.UserDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Mapper
public interface UserRepository {
    int insertUser(UserDto user);
    Optional<UserDto> findByUserId(String Id);
    Optional<UserDto> findById(long id);
    int deleteUser(long userSeq);
}
