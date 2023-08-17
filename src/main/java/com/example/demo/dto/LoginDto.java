package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;

@Data
@Builder
@AllArgsConstructor
public class LoginDto {

    @Email
    private String email;

    private String password;
}
