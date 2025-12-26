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

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> socialLogin(@RequestBody LoginRequest request) {

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

}
