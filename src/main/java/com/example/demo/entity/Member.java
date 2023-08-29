package com.example.demo.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "member")
public class Member extends BaseTimeEntity {

    @Id
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

    @OneToMany(mappedBy = "member")
    private List<Board> boardList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Reply> replyList = new ArrayList<>();

    // spring security UserDetails
    public CustomUserDetails customUserDetails() {
        return new CustomUserDetails(this);
    }

    @Override
    public String toString() {
        return "Member{" +
                "sno=" + sno +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", role=" + role +
                ", useYn=" + useYn +
                ", boardList=" + boardList +
                ", replyList=" + replyList +
                '}';
    }

    public Member() {

    }
}