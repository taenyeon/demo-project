package com.example.demoproject.domain;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class RefreshTokenDto {
    private String refreshToken;
    private Date creatAt;
    private Date expiredAt;
}
