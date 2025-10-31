package com.example.timefit.domain.user.dto;

import com.example.timefit.domain.user.entity.User;

public record UserResponse (
  Long id,
  String socialId,
  String nickname,
  String profileImage
) {
  public UserResponse(User user) {
    this(user.getId(), user.getSocialId(), user.getNickname(), user.getProfileImage());
  }
}