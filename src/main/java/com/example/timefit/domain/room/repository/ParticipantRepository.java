package com.example.timefit.domain.room.repository;

import com.example.timefit.domain.room.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    Optional<Participant> findByRoom_IdAndUser_Id(Long roomId, Long userId);
    long countByRoom_Id(Long roomId);
    long countByRoom_IdAndHasRespondedTrue(Long roomId);
    List<Participant> findByRoom_Id(Long roomId);
}