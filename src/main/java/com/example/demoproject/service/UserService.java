package com.example.demoproject.service;

import com.example.demoproject.common.security.entity.Role;
import com.example.demoproject.domain.UserDto;
import com.example.demoproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
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
        user.setRegistDateTime(LocalDateTime.now());
        return userRepository.insertUser(user);
    }
    public UserDto findByUserIdWhenLogin(String id) {
        return userRepository.findByUserId(id)
                .orElseThrow(() -> new IllegalStateException("등록된 회원을 찾을 수 없습니다."));
    }

    @Cacheable(value = "userEntity", key = "userSeq", cacheManager = "redisCacheManager")
    // Redis Cache 에 저장, 조회를 통해 DB를 사용하지 않고 더 빠르게 조회 가능하게 함.
    // Redis에 해당 데이터가 없을 경우, DB에서 조회 후, 케시로 저장.
    public UserDto findById(long userSeq) {
        log.info("getUserDto From DB");
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
