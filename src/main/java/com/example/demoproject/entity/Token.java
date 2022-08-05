package com.example.demoproject.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

public class Token {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static final class Request {
        private String id;
        private String pwd;
    }

    @SuperBuilder
    public static final class Response extends ResponseDto {
        private String token;

        private Response(ResponseDtoBuilder<?, ?> b) {
            super(b);
        }
    }
}
