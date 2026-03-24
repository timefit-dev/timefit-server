package com.example.timefit.domain.timeslot.entity;

import java.time.LocalDateTime;

import com.example.timefit.domain.room.entity.Participant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "time_slots",
        uniqueConstraints = @UniqueConstraint(columnNames = {"participant_id", "date_time"})
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TimeSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id", nullable = false)
    private Participant participant;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @Column(name = "is_available", nullable = false)
    private boolean isAvailable;

    public TimeSlot(Participant participant, LocalDateTime dateTime, boolean isAvailable) {
        this.participant = participant;
        this.dateTime = dateTime;
        this.isAvailable = isAvailable;
    }
}
