package com.example.timefit.domain.user.service;

import org.springframework.stereotype.Service;
import com.example.timefit.domain.user.dto.LoginResponse;

@Service
public class AppleOAuthService {

    public LoginResponse login(String authorizationCode) {
        System.out.println("[AppleOAuthService] 인가코드로 애플 로그인 진행");
        // TODO: 애플 연동 로직 추가
        return new LoginResponse(null, null, null);
    }
}
