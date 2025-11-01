package com.example.timefit.domain.room.dto;

import com.example.timefit.domain.room.entity.Room;
import com.example.timefit.domain.room.entity.RoomDate;
import com.example.timefit.domain.user.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record RoomResponse(
        Long id,
        String title,
        String inviteCode,
        User owner,
        List<LocalDate> dates,
        String startTime,
        String endTime,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime expiresAt)
{
    public RoomResponse(Room room) {
        this(
                room.getId(),
                room.getTitle(),
                room.getInviteCode(),
                room.getOwner(),
                room.getDates().stream()
                        .map(RoomDate::getDate)
                        .collect(Collectors.toList()),
                room.getStartTime(),
                room.getEndTime(),
                room.getCreatedAt(),
                room.getUpdatedAt(),
                room.getExpiresAt());
    }
}
