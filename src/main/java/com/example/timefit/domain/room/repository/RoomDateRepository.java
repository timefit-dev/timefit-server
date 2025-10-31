package com.example.timefit.domain.room.repository;

import com.example.timefit.domain.room.entity.Room;
import com.example.timefit.domain.room.entity.RoomDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomDateRepository extends JpaRepository<RoomDate, Long> {
    List<RoomDate> findByRoom(Room room);
}
