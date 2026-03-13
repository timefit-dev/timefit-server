package com.example.timefit.domain.user.dto;

public record UserUpdateRequest(
    String nickname,
    String profileImageUrl
) {}
