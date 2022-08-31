package com.example.demoproject.service;

import com.example.demoproject.common.security.entity.Role;
import com.example.demoproject.domain.UserDto;
import com.example.demoproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public int insertUser(UserDto user) {
        if (userRepository.findByUserId(user.getId()).isPresent()) {
            throw new IllegalStateException("중복되는 회원이 존재합니다.");
        }
        user.setPwd(passwordEncoder.encode(user.getPwd()));
        Role role = Role.valueOf(user.getRole());
        user.setRole(roleFormatStr(role));
        return userRepository.insertUser(user);
    }
    public UserDto findByUserIdWhenLogin(String id) {
        return userRepository.findByUserId(id)
                .orElseThrow(() -> new IllegalStateException("등록된 회원을 찾을 수 없습니다."));
    }

    @Cacheable(value = "userEntity", key = "userSeq", cacheManager = "redisCacheManager")
    public UserDto findById(long userSeq) {
        return userRepository.findById(userSeq)
                .orElseThrow(() -> new IllegalStateException("등록된 회원을 찾을 수 없습니다."));
    }

    @CacheEvict(value = "userEntity", key = "userSeq", cacheManager = "redisCacheManager")
    public int deleteUser(long userSeq) {
        return userRepository.deleteUser(userSeq);
    }

    private String roleFormatStr(Role role){
        List<Role> roleList = Arrays.stream(Role.values())
                .filter(includedRole -> includedRole.getOrder() >= role.getOrder())
                .collect(Collectors.toList());
        List<String> roles = new ArrayList<>();
        for (Role r : roleList){
            roles.add(r.getValue());
        }
        return String.join(",",roles);
    }

}
