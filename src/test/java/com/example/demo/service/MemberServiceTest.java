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
@DisplayName("멤버_서비스_테스트")
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
    void 회원가입() throws Exception {
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
    @ DisplayName("로그인")
    void 로그인() {
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


/* BEFORE */

//package com.example.demo.service;
//
//import com.example.demo.dto.LoginDto;
//import com.example.demo.dto.SignUpDto;
//import com.example.demo.dto.TokenDto;
//import com.example.demo.entity.Member;
//import com.example.demo.entity.Role;
//import com.example.demo.jwt.JwtProvider;
//import com.example.demo.repository.MemberRepository;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class MemberServiceTest {
//
//    @InjectMocks
//    private MemberService memberService;
//
//    @Mock
//    private MemberRepository memberRepository;
//
//    @Mock
//    private BCryptPasswordEncoder passwordEncoder;
//
//    @Mock
//    private JwtProvider jwtProvider;
//
//    @Test
//    void 회원가입() throws Exception {
//
//        // given
//        SignUpDto signUpDto = new SignUpDto();
//        signUpDto.setSno(1L);
//        signUpDto.setEmail("test11@naver.com");
//        signUpDto.setPassword("testPassword");
//        signUpDto.setCheckedPassword("testPassword");
//        signUpDto.setName("test");
//        signUpDto.setAge(25);
//        signUpDto.setRole(Role.USER);
//        signUpDto.setUseYn(0);
//
//
//        // then
//        SignUpDto result = memberService.signUp(signUpDto);
//
//        verify(memberRepository, times(1)).save(any(Member.class)); // 메서드 호출 검증
//
//        assertEquals("test11@naver.com", result.getEmail());
//        assertEquals("test", result.getName());
//        assertEquals(Role.USER, result.getRole());
//        assertEquals(25, result.getAge());
//        assertEquals(0, result.getUseYn());
//
//        System.out.println("result = " + result);
//    }
//
//
//    @Test
//    void 로그인() {
//
//        // given
//        String email = "test11@naver.com";
//        String password = "testPassword";
//
//        Member mockMember = new Member();
//        mockMember.setSno(3L);
//        mockMember.setEmail(email);
//        mockMember.setPassword(password);
//        mockMember.setPassword(passwordEncoder.encode(password));
//
//        // when
//        when(memberRepository.findByEmail(email)).thenReturn(Optional.of(mockMember));
//        when(jwtProvider.accessToken(any(Member.class))).thenReturn("mockAccessToken");
//        when(jwtProvider.refreshToken(any(Member.class))).thenReturn("mockRefreshToken");
//        when(passwordEncoder.matches(password, mockMember.getPassword())).thenReturn(true);
//
//        // then
//        LoginDto loginDto = new LoginDto(email, password);
//        TokenDto tokenDto = memberService.login(loginDto);
//
//        verify(memberRepository, times(1)).findByEmail(email);
//
//        assertNotNull(tokenDto);
//        assertEquals("mockAccessToken", tokenDto.getAccessToken());
//        assertEquals("mockRefreshToken", tokenDto.getRefreshToken());
//
//        System.out.println("loginDto = " + loginDto);
//        System.out.println("tokenDto = " + tokenDto);
//    }
//}