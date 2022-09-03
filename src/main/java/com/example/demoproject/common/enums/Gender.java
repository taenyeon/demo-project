package com.example.demoproject.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Gender {
    M("MEN"),
    W("WOMEN");

    private final String value;
}
