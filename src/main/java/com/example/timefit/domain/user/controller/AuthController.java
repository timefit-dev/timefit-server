package com.example.timefit.domain.user.controller;

import com.example.timefit.domain.user.dto.LoginRequest;
import com.example.timefit.domain.user.dto.LoginResponse;
import com.example.timefit.domain.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> socialLogin(@RequestBody LoginRequest request) {
    log.info("[AuthController] 로그인 요청 수신: provider={}, code={}", request.provider(), request.authorizationCode());
    LoginResponse response = authService.socialLogin(request);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/kakao/callback")
  public ResponseEntity<LoginResponse> kakaoCallback(@RequestParam("code") String code) {
    log.info("[AuthController] 인가코드 수신 (카카오): {}", code);
    LoginResponse response = authService.kakaoLogin(code);
    return ResponseEntity.ok(response);
  }
}