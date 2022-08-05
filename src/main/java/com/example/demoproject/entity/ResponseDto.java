package com.example.demoproject.entity;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
public class ResponseDto {
    private int resultCode;
    private String resultMessage;
}
