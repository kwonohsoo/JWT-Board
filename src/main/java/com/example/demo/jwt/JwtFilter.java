package com.example.demo.jwt;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    private final JwtProvider jwtProvider;

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String token = jwtProvider.resolveAccessToken(request);
        String requestURI = request.getRequestURI();

        if (token != null && jwtProvider.validateToken(token, true)) {
            Authentication authentication = jwtProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.info("Security context에 인증 정보를 저장했습니다. ", requestURI);
        } else {
            logger.debug("유효한 Jwt 토큰이 없거나 검증되지 않았습니다. ", requestURI);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
