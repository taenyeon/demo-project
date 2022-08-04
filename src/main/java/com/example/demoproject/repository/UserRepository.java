package com.example.demoproject.repository;

import com.example.demoproject.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Mapper
public interface UserRepository {
    int insertUser(UserEntity user);
    Optional<UserEntity> findByIdWhenLogin(String Id);
    Optional<UserEntity> findById(long id);
    int deleteUser(long userSeq);
}
