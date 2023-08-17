package com.example.demo.dto;

import com.example.demo.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import com.example.demo.entity.Role;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
public class SignUpDto {

    @NotBlank(message = "이메일을 입력해주세요")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;

    private String checkedPassword;

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @NotNull(message = "나이를 입력해주세요.")
    private int age;

    private Role role;

    private int useYn;

    @Builder
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