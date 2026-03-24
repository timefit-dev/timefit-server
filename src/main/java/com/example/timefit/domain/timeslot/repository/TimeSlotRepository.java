package com.example.timefit.domain.timeslot.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.timefit.domain.timeslot.entity.TimeSlot;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {

    void deleteByParticipantId(Long participantId);

    List<TimeSlot> findByParticipantRoomId(Long roomId);

    List<TimeSlot> findByParticipantRoomIdAndDateTime(Long roomId, LocalDateTime dateTime);

    boolean existsByParticipantIdAndDateTime(Long participantId, LocalDateTime dateTime);
}
