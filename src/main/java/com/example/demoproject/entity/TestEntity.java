package com.example.demoproject.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TestEntity {
    private String name;
    private String value;
}
