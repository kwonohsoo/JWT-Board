package com.example.demo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;

@Data
@Builder
@AllArgsConstructor
@ApiModel
public class LoginDto {

    @Email
    @ApiModelProperty(position = 1, required = true, value = "이메일", example = "test@naver.com")
    private String email;

    @ApiModelProperty(position = 2, required = true, value = "비밀번호", example = "testPassword")
    private String password;
}
