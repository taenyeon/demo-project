package com.example.demoproject.common.security;

import com.example.demoproject.common.security.entity.UserDetailCustom;
import com.example.demoproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private UserService userService;
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        return new UserDetailCustom(userService.findByUserIdWhenLogin(userId));
    }

    public UserDetailCustom loadUserById(String id){
        return new UserDetailCustom(userService.findByUserIdWhenLogin(id));
    }

    public UserDetailCustom loadUserByUserSeq(long userSeq){
        return new UserDetailCustom(userService.findById(userSeq));
    }
}
