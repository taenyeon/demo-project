package com.example.demoproject.service;

import com.example.demoproject.common.security.entity.Role;
import com.example.demoproject.entity.UserEntity;
import com.example.demoproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public int insertUser(UserEntity user) {
        if (userRepository.findByIdWhenLogin(user.getId()).isPresent()) {
            throw new IllegalStateException("중복되는 회원이 존재합니다.");
        }
        user.setPwd(passwordEncoder.encode(user.getPwd()));
        Role role = Role.valueOf(user.getRole());
        List<Role> roleList = Arrays.stream(Role.values())
                .filter(includedRole -> includedRole.getOrder() >= role.getOrder())
                .collect(Collectors.toList());
        List<String> roles = new ArrayList<>();
        for (Role r : roleList){
            roles.add(r.getValue());
        }
        user.setRole(String.join(",",roles));
        return userRepository.insertUser(user);
    }

    public UserEntity findByIdWhenLogin(String id) {
        return userRepository.findByIdWhenLogin(id)
                .orElseThrow(() -> new IllegalStateException("등록된 회원을 찾을 수 없습니다."));
    }

    public UserEntity findById(long userSeq) {
        return userRepository.findById(userSeq)
                .orElseThrow(() -> new IllegalStateException("등록된 회원을 찾을 수 없습니다."));
    }


    public int deleteUser(long userSeq) {
        return userRepository.deleteUser(userSeq);
    }

}
