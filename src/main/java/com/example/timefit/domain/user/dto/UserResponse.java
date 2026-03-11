package com.example.timefit.domain.user.dto;

import com.example.timefit.domain.user.entity.User;

public record UserResponse(
        Long id,
        String nickname,
        String provider,
        String profileImageUrl
        ) {

    public UserResponse(User user) {
        this(user.getId(), user.getNickname(), user.getProvider(), user.getProfileImageUrl());
    }
}
