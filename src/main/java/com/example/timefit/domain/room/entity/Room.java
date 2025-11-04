package com.example.timefit.domain.room.entity;

import com.example.timefit.domain.user.entity.User;
import com.example.timefit.domain.room.dto.RoomUpdateRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private User owner;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomDate> dates = new ArrayList<>();

    @Column(name = "start_time", length = 20, nullable = false)
    private String startTime;

    @Column(name = "end_time", length = 20, nullable = false)
    private String endTime;

    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    public Room(String title, String inviteCode, User owner, String startTime, String endTime) {
        this.title = title;
        this.inviteCode = inviteCode;
        this.owner = owner;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public void onPrePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        this.expiresAt = now.plusDays(2);
    }

    public void patchUpdate(RoomUpdateRequest dto) {
        boolean updated = false;

        if (dto.getTitle() != null && !dto.getTitle().isBlank()) {
            this.title = dto.getTitle();
            updated = true;
        }

        if (dto.getStartTime() != null) {
            this.startTime = dto.getStartTime();
            updated = true;
        }

        if (dto.getEndTime() != null) {
            this.endTime = dto.getEndTime();
            updated = true;
        }

        if (dto.getDates() != null) {
            this.dates.clear();
            for (LocalDate date : dto.getDates()) {
                this.dates.add(new RoomDate(date, this)); // (DB에 새 날짜 INSERT)
            }
            updated = true;
        }

        if (updated) {
            this.updatedAt = LocalDateTime.now();
        }
    }
}
