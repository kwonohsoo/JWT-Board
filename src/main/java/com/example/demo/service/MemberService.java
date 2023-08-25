package com.example.demo.service;

import com.example.demo.dto.LoginDto;
import com.example.demo.dto.SignUpDto;
import com.example.demo.dto.TokenDto;
import com.example.demo.entity.Member;
import com.example.demo.entity.Role;
import com.example.demo.jwt.JwtProvider;
import com.example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    // 회원가입
    @Transactional
    public SignUpDto signUp(SignUpDto signUpDto) throws Exception {

        if (memberRepository.findByEmail(signUpDto.getEmail()).isPresent()) {
            log.info("이미 존재하는 이메일 입니다.");
            throw new Exception("이미 존재하는 이메일입니다.");
        }

        if (!signUpDto.getPassword().equals(signUpDto.getCheckedPassword())) {
            log.info("비밀번호가 일치하지 않습니다.");
            throw new Exception("비밀번호가 일치하지 않습니다.");
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        signUpDto.setPassword(encoder.encode(signUpDto.getPassword()));
        signUpDto.setRole(Role.USER);
        signUpDto.setUseYn(signUpDto.getUseYn());

        memberRepository.save(signUpDto.toEntity());

        return signUpDto;
    }

    // 로그인
    public TokenDto login(LoginDto loginDto) {

        String email = loginDto.getEmail();
        String password = loginDto.getPassword();

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("해당하는 사용자가 없습니다."));

        if (passwordEncoder.matches(password, member.getPassword())) {
            String accessToken = jwtProvider.accessToken(member);
            String refreshToken = jwtProvider.refreshToken(member);

            return new TokenDto(accessToken, refreshToken);
        }
        throw new RuntimeException("비밀번호가 일치하지 않습니다.");
    }
}