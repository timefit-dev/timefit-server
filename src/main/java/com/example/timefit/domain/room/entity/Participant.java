package com.example.timefit.domain.room.entity;

import com.example.timefit.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
  name = "participants",
  uniqueConstraints = @UniqueConstraint(columnNames = {"room_id", "user_id"})
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Participant {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "room_id", nullable = false)
  private Room room;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(name = "has_responded", nullable = false)
  private boolean hasResponded = false;

  @Column(name = "joined_at", nullable = false, updatable = false)
  private LocalDateTime joinedAt;

  @Column(name = "responded_at")
  private LocalDateTime respondedAt;

  public Participant(Room room, User user) {
    this.room = room;
    this.user = user;
    this.joinedAt = LocalDateTime.now();
  }

  public void markResponded() {
    this.hasResponded = true;
    this.respondedAt = LocalDateTime.now();
  }
  
}
