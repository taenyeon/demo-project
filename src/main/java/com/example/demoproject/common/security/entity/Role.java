package com.example.demoproject.common.security.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {
    ADMIN("ROLE_ADMIN",1),
    USER("ROLE_USER",2);

    private final String value;
    private final int order;
}
