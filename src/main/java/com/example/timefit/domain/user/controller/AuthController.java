package com.example.timefit.domain.user.controller;

import com.example.timefit.domain.user.dto.LoginRequest;
import com.example.timefit.domain.user.dto.LoginResponse;
import com.example.timefit.domain.user.service.AuthService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/kakao")
    public ResponseEntity<LoginResponse> kakaoLogin(@RequestBody LoginRequest request) {

        LoginResponse response = authService.loginWithKakao(request.accessToken());
        return ResponseEntity.ok(response);
    }
}
