package com.example.timefit.domain.room.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "room_dates", uniqueConstraints = {
        @UniqueConstraint(
                name = "DATE_ROOM_UNIQUE", // DB에 표시될 제약조건 이름
                columnNames = {"room_id", "date"} // 이 두 컬럼의 조합은 유일해야 함
        )
})
public class RoomDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id") // bigint
    private Long id;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Builder
    public RoomDate(LocalDate date, Room room) {
        this.date = date;
        this.room = room;
    }
}
