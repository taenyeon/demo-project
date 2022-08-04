package com.example.demoproject.repository;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface TestRepository {
    int selectTestNum(int num);
}
