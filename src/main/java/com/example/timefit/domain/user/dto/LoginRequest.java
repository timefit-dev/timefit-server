package com.example.timefit.domain.user.dto;

public record LoginRequest(
        String provider,
        String accessToken
        ) {

}
