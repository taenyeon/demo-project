package com.example.demoproject.domain;

import com.example.demoproject.common.enums.Gender;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class UserDto {
    @Id
    private Long userSeq;
    @NotBlank(message = "id 값은 필수 입력값입니다.")
    private String id;
    @NotBlank(message = "pwd 값은 필수 입력값입니다.")
    @Pattern(message = "pwd는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.", regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$")
    private String pwd;
    private String name;
    private Gender gender;
    @JsonFormat(pattern="yyyy-MM-dd", timezone = "Asia/Seoul", shape = JsonFormat.Shape.STRING)
    private LocalDate birthDay;
    @NotBlank(message = "phoneNum 값은 필수 입력값입니다.")
    @Pattern(message = "phoneNum 형식을 사용하세요.", regexp = "\\d{3}-\\d{4}-\\d{4}")
    private String phoneNum;
    @NotBlank(message = "email 값은 필수 입력값입니다.")
    @Pattern(message = "email 형식을 사용하세요.",regexp = "^(.+)@(.+)$")
    private String email;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime registDateTime;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifyDateTime;
    private String role;
}
