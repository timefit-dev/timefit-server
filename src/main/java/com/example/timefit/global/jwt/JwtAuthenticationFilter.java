package com.example.timefit.global.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.lang.NonNull;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String requestUri = request.getRequestURI();

        // ✅ 인증 관련 API는 JWT 필터 제외
        if (requestUri.startsWith("/api/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = resolveToken(request);

        // ✅ 토큰이 없으면 그냥 통과 (SecurityConfig에서 막힘)
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // ✅ 토큰 검증
        if (!jwtTokenProvider.validateToken(token)) {
            log.warn("[JwtAuthenticationFilter] JWT가 유효하지 않습니다");
            filterChain.doFilter(request, response);
            return;
        }

        // ✅ 인증 객체 생성 및 저장
        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.info(
                "[JwtAuthenticationFilter] 인증 성공 - userId={}",
                authentication.getPrincipal()
        );

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }
}
