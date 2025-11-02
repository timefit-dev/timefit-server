package com.example.timefit.domain.user.dto;

public record AuthRequest(
  String provider,
  String authorizationCode
) {}
