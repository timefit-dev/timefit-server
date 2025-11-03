package com.example.timefit.domain.user.service;

import org.springframework.stereotype.Service;

import com.example.timefit.domain.user.dto.LoginResponse;

@Service
public class KakaoOAuthService {

    public LoginResponse login(String authorizationCode) {
        System.out.println("[KakaoOAuthService] 인가코드로 카카오 로그인 진행");
        // TODO: 카카오 API 연동 로직 추가
        return new LoginResponse(null, null, null);
    }
}
