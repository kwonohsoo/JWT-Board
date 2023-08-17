package com.example.demo.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class Member extends BaseTimeEntity implements UserDetails {

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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.name()));

        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}