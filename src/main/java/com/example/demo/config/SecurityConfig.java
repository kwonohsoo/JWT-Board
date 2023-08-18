package com.example.demo.config;

import com.example.demo.jwt.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // Spring MVC & Spring Security 통합   // @EnableWebMvcSecurity (Spring Security 4.0부터 사용 X)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProvider jwtProvider;

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf().disable() // csrf 공격 방지, 비활성화
                .httpBasic().disable() // http 기본 인증, 비활성화
                .formLogin().disable() // 폼 기반 로그인, 비활성화
                .addFilterBefore(new JwtFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class) // jwt 처리
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 관리 설정, 세션 생성 x

                .and()
                .authorizeRequests() // 요청에 대한 접근 권한
                .antMatchers("/api/**").permitAll() // 모든 사용자 접근 가능
                .antMatchers("/**").permitAll() // 모든 사용자 접근 가능

                // 예외 처리
                .and()
                .apply(new JwtConfig(jwtProvider)) // jwt Config 적용

                .and()
                .exceptionHandling().accessDeniedHandler(new JwtAccessDeniedHandler()) // 접근이 거부되었을 때 호출되는 핸들러, 403

                .and()
                .exceptionHandling().authenticationEntryPoint(new JwtAuthenticationEntryPoint()); // 인증이 실패 했을 때 호출되는 핸들러, 401

        return http.build();
    }
}
