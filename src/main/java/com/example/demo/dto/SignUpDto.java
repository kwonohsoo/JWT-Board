package com.example.demo.dto;

import com.example.demo.entity.Member;
import com.example.demo.entity.Role;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
@Builder
@AllArgsConstructor
@ApiModel(value = "회원가입 응답 정보")
public class SignUpDto {

    @ApiModelProperty(position = 1, required = true, value = "회원 번호", example = "1")
    private Long sno;

    @NotBlank(message = "이메일을 입력해주세요")
    @ApiModelProperty(position = 2, required = true, value = "이메일", example = "test@naver.com")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요")
    @ApiModelProperty(position = 3, required = true, value = "비밀번호", example = "testPassword")
    private String password;

    @NotBlank(message = "일치하는 비밀번호를 입력해주세요")
    @ApiModelProperty(position = 4, required = true, value = "비밀번호 확인", example = "testPassword")
    private String checkedPassword;

    @NotBlank(message = "이름을 입력해주세요.")
    @ApiModelProperty(position = 5, required = true, value = "이름", example = "권오수")
    private String name;

    @NotNull(message = "나이를 입력해주세요.")
    @ApiModelProperty(position = 6, required = true, value = "나이", example = "20")
    private int age;

    @ApiModelProperty(position = 7, value = "권한", example = "USER")
    private Role role;

    @ApiModelProperty(position = 8, value = "사용여부", example = "사용: 0, 삭제: 9")
    private int useYn;

    public SignUpDto() {

    }


    public Member toEntity() {
        return Member.builder()
                .email(email)
                .name(name)
                .age(age)
                .password(password)
                .role(Role.USER)
                .useYn(useYn)
                .build();
    }
}