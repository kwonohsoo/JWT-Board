package com.example.demo.controller;

import com.example.demo.dto.LoginDto;
import com.example.demo.dto.SignUpDto;
import com.example.demo.dto.TokenDto;
import com.example.demo.entity.Member;
import com.example.demo.jwt.JwtProvider;
import com.example.demo.repository.MemberRepository;
import com.example.demo.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
//@SpringBootTest
@DisplayName("멤버_컨트롤러_테스트")
class MemberControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private MemberController memberController;

    @Mock
    private MemberService memberService;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private MemberRepository memberRepository;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(memberController).build();
    }

    @Test
    @DisplayName("회원 가입")
    void 회원_가입() throws Exception {
        // given
        SignUpDto signUpDto = SignUpDto.builder()
                .email("test@naver.com")
                .password("testPassword")
                .checkedPassword("testPassword")
                .name("test")
                .age(20)
                .build();

        // when
        when(memberService.signUp(any(SignUpDto.class))).thenReturn(signUpDto);
        ResponseEntity<SignUpDto> responseEntity = memberController.join(signUpDto);

        // then
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
        assertThat(responseEntity.getBody()).isEqualTo(signUpDto);
    }

    @Test
    @DisplayName("로그인")
    void 로그인() {
        // given
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("test@naver.com");
        loginDto.setPassword("testPassword");

        TokenDto tokenDto = new TokenDto("EncodedAccessToken", "EncodedRefreshToken");

        // when
        when(memberService.login(any(LoginDto.class))).thenReturn(tokenDto);
        ResponseEntity<TokenDto> responseEntity = memberController.login(loginDto);

        // then
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
        assertThat(responseEntity.getBody()).isEqualTo(tokenDto);
    }

    @Test
    void 토큰_생성() {
        // Given
        Member member = new Member();
        member.setEmail("test@naver.com");

        when(jwtProvider.accessToken(member)).thenReturn("newAccessToken");

        // When
        String newAccessToken = jwtProvider.accessToken(member);

        // Then
        assertNotNull(newAccessToken);
        verify(jwtProvider, times(1)).accessToken(member);
    }

    @Test
    @DisplayName("토큰 갱신")
    void 토큰_갱신() throws Exception {
        // given 변수 초기화
        String refreshToken = "refreshToken";
        String newAccessToken = "newAccessToken";

        Member member = new Member();
        member.setEmail("test@naver.com"); // 객체에 이메일 설정

        // when 실제 테스트 진행
        when(jwtProvider.validateToken(refreshToken, true)).thenReturn(true); // 메서드 호출될 때마다 true 반환
        when(jwtProvider.getEmail(refreshToken)).thenReturn("test@naver.com");
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(member));
        when(jwtProvider.accessToken(member)).thenReturn(newAccessToken); // 메서드 호출될 때 새로운 토큰 반환

        // then 예상 결과 검증
        mockMvc.perform(MockMvcRequestBuilders.post("/api/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"refreshToken\": \"" + refreshToken + "\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk()) // 200 맞는지 확인
                .andExpect(MockMvcResultMatchers.jsonPath("$.accessToken").value(newAccessToken))
                .andExpect(MockMvcResultMatchers.jsonPath("$.refreshToken").value(refreshToken)); // 값 검증

        verify(jwtProvider, times(1)).validateToken(refreshToken, true);
        verify(jwtProvider, times(1)).getEmail(refreshToken);
        verify(memberRepository, times(1)).findByEmail(anyString());
        verify(jwtProvider, times(1)).accessToken(member);
    }
}