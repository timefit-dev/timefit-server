package com.example.timefit.domain.room.entity;

import com.example.timefit.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "rooms")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", length = 100, nullable = false)
    private String title;

    @Column(name = "invite_code", length = 10, nullable = false, unique = true)
    private String inviteCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User ownerId;

    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Builder
    public Room(String title, String inviteCode, User owner) {
        this.title = title;
        this.inviteCode = inviteCode;
        this.ownerId = owner;
    }

    // JPA가 이 엔티티를 DB에 'INSERT' 쿼리를 날리기 직전에 호출됩니다.
    @PrePersist
    public void onPrePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        this.expiresAt = now.plusDays(2); // 생성기한 + 2일
    }

     // JPA가 이 엔티티를 'UPDATE' 쿼리를 날리기 직전에 호출됩니다.
    @PreUpdate
    public void onPreUpdate() {
        LocalDateTime now = LocalDateTime.now();
        this.updatedAt = now;
        this.expiresAt = now.plusDays(2); // 수정기한 + 2일
    }
}
