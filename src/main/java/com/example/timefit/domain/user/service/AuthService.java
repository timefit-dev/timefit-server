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

    log.info("[AuthService] 로그인 요청 - provider: {}, code: {}", provider, code);
    
    SocialLoginService service = socialLoginServices.stream()
            .filter(s -> s.getProviderName().equals(provider))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 로그인 방식입니다: " + provider));
            
    return service.login(code);
  }
}
