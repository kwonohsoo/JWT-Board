package com.example.demo.jwt;

import com.example.demo.entity.Member;
import com.example.demo.service.CustomUserDetailsService;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final CustomUserDetailsService userDetailsService;
    private final RedisTemplate<String, String> redisTemplate;
    private final Logger logger = LoggerFactory.getLogger(JwtProvider.class);


    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.headerAccessToken}")
    private String headerAccessToken;

    @Value("${jwt.headerRefreshToken}")
    private String headerRefreshToken;

    @Value("${jwt.accessTokenValidMillisecond}")
    private long accessTokenValidMillisecond;

    @Value("${jwt.refreshTokenValidMillisecond}")
    private long refreshTokenValidMillisecond;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // AccessToken CREATE
    public String accessToken(Member member) {
        List<String> roles = member.customUserDetails()
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        Map<String, Object> map = new HashMap<>();
        map.put("headerAccessToken", "Bearer"); // 예시: Bearer

        return Jwts.builder()
                .setHeader(map)
                .setSubject(member.getEmail())
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenValidMillisecond))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    // RefreshToken CREATE
    public String refreshToken(Member member) {
        Claims claims = Jwts.claims().setSubject(member.getEmail());
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + refreshTokenValidMillisecond);

        Map<String, Object> map = new HashMap<>();
        map.put("headerRefreshToken", headerRefreshToken);

        String refreshToken = Jwts.builder()
                .setHeader(map)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();

        // Redis RefreshToken 저장
        redisTemplate.opsForValue().set(
                member.getEmail(),
                refreshToken,
                refreshTokenValidMillisecond,
                TimeUnit.MILLISECONDS
        );

        return refreshToken;
    }

    public void deleteRefreshToken(String email) {
        redisTemplate.delete(email);
    }

    // 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getEmail(token));

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 유저 정보 추출(이메일)
    public String getEmail(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Request Header에서 AccessToken 꺼내옴
    public String resolveAccessToken(HttpServletRequest request) {
        return request.getHeader(headerAccessToken);
    }

    // Token 유효성 검사
    public boolean validateToken(String token, boolean isRefreshToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            if (isRefreshToken) {
                return true;
            }
            return !claims.getBody().getExpiration().before(new Date());

        } catch (SecurityException | MalformedJwtException e) {
            logger.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            logger.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            logger.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            logger.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }
}
