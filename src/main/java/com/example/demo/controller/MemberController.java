package com.example.demo.controller;

import com.example.demo.dto.LoginDto;
import com.example.demo.dto.SignUpDto;
import com.example.demo.dto.TokenDto;
import com.example.demo.entity.Member;
import com.example.demo.jwt.JwtProvider;
import com.example.demo.repository.MemberRepository;
import com.example.demo.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class MemberController {

    private final Logger logger = LoggerFactory.getLogger(MemberController.class);

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;


    // 회원가입
    @PostMapping("/join")
    public ResponseEntity<SignUpDto> join(@Valid @RequestBody SignUpDto signUpDto) {
        try {
            SignUpDto join = memberService.signUp(signUpDto);
            System.out.println("회원가입 : " + join);
            return ResponseEntity.ok(signUpDto);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 error
        }
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@Valid @RequestBody LoginDto loginDto) {
        try {
            TokenDto tokens = memberService.login(loginDto);
            System.out.println("로그인 : " + loginDto);
            System.out.println("토큰 : " + tokens);
            return ResponseEntity.ok(tokens);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    // accessToken 갱신
    @PostMapping("/refresh")
    public ResponseEntity<TokenDto> refreshAccessToken(@RequestBody TokenDto tokenDto) {
        String refreshToken = tokenDto.getRefreshToken();

        try {
            jwtProvider.validateToken(refreshToken, true);

            String email = jwtProvider.getEmail(refreshToken);
            Member member = memberRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

            String newAccessToken = jwtProvider.accessToken(member);

            System.out.println("갱신된 토큰 : " + newAccessToken);

            return ResponseEntity.ok(new TokenDto(newAccessToken, refreshToken));
        } catch (NoSuchElementException e) {
            logger.debug("사용자를 찾을 수 없습니다.: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            logger.debug("Refresh Token 유효성 검사 실패: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }


}
