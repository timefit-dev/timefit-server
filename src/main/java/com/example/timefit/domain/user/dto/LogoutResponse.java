package com.example.timefit.domain.user.dto;

import java.time.Instant;

public record LogoutResponse(
    String message,
    Instant timestamp
) {}
