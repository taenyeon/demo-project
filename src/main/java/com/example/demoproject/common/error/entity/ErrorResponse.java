package com.example.demoproject.common.error.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {
//    private String code;
    private String resultMessage;
    private int resultCode;
}
