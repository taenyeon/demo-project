package com.example.demoproject.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserEntity {
    private Long userSeq;
    private String id;
    private String pwd;
    private String role;
}
