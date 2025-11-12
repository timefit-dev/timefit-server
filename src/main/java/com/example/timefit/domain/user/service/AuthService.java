package com.example.timefit.domain.user.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.timefit.domain.user.dto.LoginRequest;
import com.example.timefit.domain.user.dto.LoginResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final List<SocialLoginService> socialLoginServices;

    public LoginResponse socialLogin(LoginRequest request) {
        String provider = request.provider().toLowerCase();
        String code = request.authorizationCode();
        log.info("[AuthService] [LOGIN_REQUEST] provider={}, code={}", provider, code);

        return getService(provider).login(code);
    }

    public LoginResponse kakaoLogin(String authorizationCode) {
        log.info("[AuthService] [KAKAO_CALLBACK] code={}", authorizationCode);
        return getService("kakao").login(authorizationCode);
    }

    private SocialLoginService getService(String provider) {
        return socialLoginServices.stream()
                .filter(s -> s.getProviderName().equalsIgnoreCase(provider))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("[AuthService] 지원되지 않는 로그인 서비스: " + provider));
    }
}
