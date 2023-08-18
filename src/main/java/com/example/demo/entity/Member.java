package com.example.demo.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class Member extends BaseTimeEntity {

    @Id
    @Column(name = "sno")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("회원 번호")
    private Long sno;

    @Column(length = 45, unique = true)
    @Comment("이메일(회원 아이디)")
    private String email;

    @Column(length = 500)
    @Comment("비밀번호")
    private String password;

    @Column(length = 45)
    @Comment("회원 이름")
    private String name;

    @Comment("나이")
    private int age;

    @Enumerated(EnumType.STRING)
    @Comment("권한")
    private Role role;

    @ColumnDefault("0")
    @Comment("사용 여부 - 사용: 0 삭제: 9")
    private int useYn;

    // spring security UserDetails
    public CustomUserDetails customUserDetails() {
        return new CustomUserDetails(this);
    }
}