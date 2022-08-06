package com.example.demoproject.common.security.entity;

import com.example.demoproject.entity.UserEntity;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@Getter
@Setter
@ToString
public class UserDetailCustom implements UserDetails {
    private long userSeq;
    private String id;
    private String pwd;
    private String role;

    public UserDetailCustom(UserEntity userEntity) {
        this.userSeq = userEntity.getUserSeq();
        this.id = userEntity.getId();
        this.pwd = userEntity.getPwd();
        this.role = userEntity.getRole();
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
        return pwd;
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
