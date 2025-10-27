package com.example.timefit.domain.user.dto;

import com.example.timefit.domain.user.entity.User;
import lombok.Getter;

@Getter
public class UserResponse {
  private final Long id;
  private final String socialId;
  private final String nickname;
  private final String profileImage;

  public UserResponse(User user) {
    this.id = user.getId();
    this.socialId = user.getSocialId();
    this.nickname = user.getNickname();
    this.profileImage = user.getProfileImage();
  }

}
