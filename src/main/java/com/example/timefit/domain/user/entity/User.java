package com.example.timefit.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

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
  private String profileImage;

  @Column(updatable = false)
  private LocalDateTime createdAt;

  @Column
  private LocalDateTime updatedAt;
}
