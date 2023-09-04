package com.example.demo.service;

import com.example.demo.dto.LoginDto;
import com.example.demo.dto.SignUpDto;
import com.example.demo.dto.TokenDto;
import com.example.demo.entity.Member;
import com.example.demo.entity.Role;
import com.example.demo.jwt.JwtProvider;
import com.example.demo.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//@SpringBootTest // 모든 빈을 IoC에 등록하기 때문에 속도 느림, 전체 테스트 할 때 사용 (ex. 전역에 redis 설정을 안해서 오류남 but @ExtendWith(MockitoExtension.class)은 필요한 객체만 설정해서 redis 오류 안남)
@ExtendWith(MockitoExtension.class) // 필요한 서비스 객체만 생성 -> IoC 등록을 하지 않아 속도 빠름
@DisplayName("Member Service TEST")
class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private JwtProvider jwtProvider;

    private SignUpDto signUpDto;
    private Member mockMember;
    private LoginDto loginDto;

    @BeforeEach
    @DisplayName("겍체 초기화")
    void setUp() {
        // 테스트 전에 실행되는 메서드, 테스트 데이터와 목 객체를 생성
        // 여러 테스트에서 사용될 객체들을 미리 초기화
        signUpDto = new SignUpDto();
        signUpDto.setSno(1L);
        signUpDto.setEmail("test11@naver.com");
        signUpDto.setPassword("testPassword");
        signUpDto.setCheckedPassword("testPassword");
        signUpDto.setName("test");
        signUpDto.setAge(25);
        signUpDto.setRole(Role.USER);
        signUpDto.setUseYn(0);

        mockMember = new Member();
        mockMember.setSno(3L);
        mockMember.setEmail(signUpDto.getEmail());
        mockMember.setPassword(signUpDto.getPassword());
        mockMember.setPassword(passwordEncoder.encode(signUpDto.getPassword()));

        loginDto = new LoginDto(signUpDto.getEmail(), signUpDto.getPassword());
    }

    @Test
    @DisplayName("회원 가입")
    void join() throws Exception {
        // given
        // setUp()

        // when (호출되었을 때(when), 반환 값 설정(thenReturn))
        when(memberRepository.save(any(Member.class))).thenReturn(mockMember);
        SignUpDto result = memberService.signUp(signUpDto);

        // then (매서드 호출하여 검증하고(verify), 결과 값들을 비교(assertAll() -> assertEquals())) ex) assertTrue, asserFalse
        verify(memberRepository, times(1)).save(any(Member.class)); // 1번 호출되었는지 검증하고, 결과 값들을 비교

        assertAll(
                () -> assertEquals(signUpDto.getEmail(), result.getEmail()),
                () -> assertEquals(signUpDto.getName(), result.getName()),
                () -> assertEquals(signUpDto.getRole(), result.getRole()),
                () -> assertEquals(signUpDto.getAge(), result.getAge()),
                () -> assertEquals(signUpDto.getUseYn(), result.getUseYn())
        );
    }

    @Test
    @DisplayName("로그인")
    void login() {
        // given
        String email = signUpDto.getEmail();
        String password = signUpDto.getPassword();

        // when (호출되었을 때(when), 반환 값 설정(thenReturn))
        when(memberRepository.findByEmail(email)).thenReturn(Optional.of(mockMember));
        when(jwtProvider.accessToken(any(Member.class))).thenReturn("mockAccessToken");
        when(jwtProvider.refreshToken(any(Member.class))).thenReturn("mockRefreshToken");
        when(passwordEncoder.matches(password, mockMember.getPassword())).thenReturn(true);

        // then (매서드 호출하여 검증하고(verify), 결과 값들을 비교(assertAll() -> assertEquals())) ex) assertTrue, asserFalse
        TokenDto tokenDto = memberService.login(loginDto);

        verify(memberRepository, times(1)).findByEmail(email);
        assertAll(
                () -> assertNotNull(tokenDto),
                () -> assertEquals("mockAccessToken", tokenDto.getAccessToken()),
                () -> assertEquals("mockRefreshToken", tokenDto.getRefreshToken())
        );
    }
}