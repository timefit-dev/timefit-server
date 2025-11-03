package com.example.timefit.domain.user.service;

import org.springframework.stereotype.Service;
import com.example.timefit.domain.user.dto.LoginResponse;

@Service
public class GoogleOAuthService {

    public LoginResponse login(String authorizationCode) {
        System.out.println("[GoogleOAuthService] 인가코드로 구글 로그인 진행");
        // TODO: 구글 API 연동 로직 추가
        return new LoginResponse(null, null, null);
    }
}