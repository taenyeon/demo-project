package com.example.demoproject.service;

import com.example.demoproject.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestService {
    private final TestRepository testRepository;

    public int selectTestNum(int num){
        return testRepository.selectTestNum(num);
    };
}
