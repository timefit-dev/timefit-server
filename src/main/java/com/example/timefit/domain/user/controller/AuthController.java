package com.example.timefit.domain.user.controller;

import com.example.timefit.domain.user.dto.LoginRequest;
import com.example.timefit.domain.user.dto.LoginResponse;
import com.example.timefit.domain.user.dto.LogoutResponse;
import com.example.timefit.domain.user.service.AuthService;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> socialLogin(@RequestBody LoginRequest request) {

        log.info("[AuthController] /api/auth/login 호출됨. provider={}", request.provider());
        LoginResponse response = authService.socialLogin(
                request.provider(),
                request.accessToken()
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(
            @RequestHeader("Authorization") String authorization
    ) {
        String refreshToken = authorization.substring(7);
        LoginResponse response = authService.reissueAccessToken(refreshToken);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logout(
            @RequestHeader("Authorization") String token
    ) {
        String accessToken = token.substring(7);

        log.info("[AuthController] /api/auth/logout 호출됨");

        authService.logout(accessToken);

        return ResponseEntity.ok(
            new LogoutResponse(
                "Logout successful",
                Instant.now()
            )
        );
    }
}
