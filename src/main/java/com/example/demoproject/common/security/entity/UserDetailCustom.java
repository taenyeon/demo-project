package com.example.demoproject.common.security.entity;

import com.example.demoproject.domain.UserDto;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@Getter
@Setter
@ToString
@RedisHash("userSeq")
public class UserDetailCustom implements UserDetails {
    @Id
    private long userSeq;
    private String id;
    private String role;

    public UserDetailCustom(UserDto userDto) {
        this.userSeq = userDto.getUserSeq();
        this.id = userDto.getId();
        this.role = userDto.getRole();
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
