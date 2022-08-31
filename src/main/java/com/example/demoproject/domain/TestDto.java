package com.example.demoproject.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TestDto {
    private String name;
    private String value;
}
