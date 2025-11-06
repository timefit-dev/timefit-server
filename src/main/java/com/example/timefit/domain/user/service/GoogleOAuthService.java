package com.example.timefit.domain.user.service;

import org.springframework.stereotype.Service;
import com.example.timefit.domain.user.dto.LoginResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GoogleOAuthService implements SocialLoginService {

    @Override
    public String getProviderName() {
        return "google";
    }

    @Override
    public LoginResponse login(String authorizationCode) {
        log.info("[GoogleOAuthService] 인가코드로 구글 로그인 진행 - code: {}", authorizationCode);
        // TODO: 구글 API 연동 로직 추가
        return new LoginResponse(null, null, null);
    }
}
