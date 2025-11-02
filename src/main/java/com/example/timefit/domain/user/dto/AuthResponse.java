package com.example.timefit.domain.user.dto;

public record AuthResponse(
    String accessToken,
    String refreshToken,
    UserResponse user
) {}
