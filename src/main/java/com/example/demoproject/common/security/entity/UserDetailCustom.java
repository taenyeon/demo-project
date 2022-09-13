package com.example.demoproject.common.security.entity;

import com.example.demoproject.domain.UserDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@Data
@RedisHash("userSeq")
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailCustom implements UserDetails {
    @Id
    private long userSeq;
    private String id;
    private String role;
    private String name;
    private String phoneNum;
    private String email;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime registDateTime;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifyDateTime;

    public UserDetailCustom(UserDto userDto) {
        this.userSeq = userDto.getUserSeq();
        this.id = userDto.getId();
        this.role = userDto.getRole();
        this.name = userDto.getName();
//        this.birthDay = userDto.getBirthDay();
        this.phoneNum = userDto.getPhoneNum();
        this.email = userDto.getEmail();
        this.registDateTime = userDto.getRegistDateTime();
        this.modifyDateTime = userDto.getModifyDateTime();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        Arrays.stream(role.split(",")).forEach(simpleRole ->{
            authorities.add(new SimpleGrantedAuthority(simpleRole));
        });
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return id;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
