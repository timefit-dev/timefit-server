package com.example.timefit.domain.timeslot.repository;

import com.example.timefit.domain.timeslot.entity.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
  void deleteByParticipantId(Long participantId);
  List<TimeSlot> findByParticipantRoomId(Long roomId);
  List<TimeSlot> findByParticipantRoomIdAndDateTime(Long roomId, LocalDateTime dateTime);
  boolean existsByParticipantIdAndDateTime(Long participantId, LocalDateTime dateTime);
}
