package com.example.timefit.domain.user.service;

import org.springframework.stereotype.Service;

import com.example.timefit.domain.user.dto.LoginRequest;
import com.example.timefit.domain.user.dto.LoginResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

  private final KakaoOAuthService kakaoOAuthService;
  private final GoogleOAuthService googleOAuthService;
  private final AppleOAuthService appleOAuthService;
  
  public LoginResponse socialLogin(LoginRequest request) {
    String provider = request.provider();
    
    switch (provider.toLowerCase()) {
      case "kakao" -> {
        log.info("[AuthService] 카카오 로그인 진입");
        return kakaoOAuthService.login(request.authorizationCode());
      }
      case "google" -> {
        log.info("[AuthService] 구글 로그인 진입");
        return googleOAuthService.login(request.authorizationCode());
      }
      case "apple" -> {
        log.info("[AuthService] 애플 로그인 진입");
        return appleOAuthService.login(request.authorizationCode());
      }
      default -> throw new IllegalArgumentException("지원하지 않는 로그인 방식입니다: " + provider);
    }
  }
}
