package com.example.timefit.domain.user.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor

public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String socialId;

  @Column(nullable = false)
  private String provider;

  @Column(nullable = false)
  private String nickname;

  @Column
  private String profileImageUrl;

  @Column(updatable = false)
  private LocalDateTime createdAt;

  @Column
  private LocalDateTime updatedAt;

  public User(String socialId, String provider, String nickname) {
    this.socialId = socialId;
    this.provider = provider;
    this.nickname = nickname;
  }

  public void updateNickname(String nickname) {
    if (nickname == null || nickname.isBlank()) {
      throw new IllegalArgumentException("nickname은 필수입니다.");
    }
    this.nickname = nickname;
    this.updatedAt = LocalDateTime.now();
  }

  public void updateProfileImage(String profileImageUrl) {
    this.profileImageUrl = profileImageUrl;
    this.updatedAt = LocalDateTime.now();
  }

}
