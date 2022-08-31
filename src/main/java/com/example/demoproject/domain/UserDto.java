package com.example.demoproject.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Builder
public class UserDto {
    @Id
    private Long userSeq;
    @NotBlank(message = "id 값은 필수 입력값입니다.")
    @Pattern(message = "id는 4~10자 영문,숫자를 사용하세요.", regexp = "^{4,10}$")
    private String id;
    @NotBlank(message = "pwd 값은 필수 입력값입니다.")
    @Pattern(message = "pwd는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.", regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$\n")
    private String pwd;
    private String role;
}
