package com.example.timefit.domain.user.service;

import org.springframework.stereotype.Service;
import com.example.timefit.domain.user.dto.LoginResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KakaoOAuthService implements SocialLoginService {

    @Override
    public String getProviderName() {
        return "kakao";
    }

    @Override
    public LoginResponse login(String authorizationCode) {
        log.info("[KakaoOAuthService] 인가코드로 카카오 로그인 진행 - code: {}", authorizationCode);
        // TODO: 카카오 API 연동 로직 추가
        return new LoginResponse(null, null, null);
    }
}
