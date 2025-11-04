package com.example.timefit.domain.room.dto;

import com.example.timefit.domain.room.entity.Room;
import com.example.timefit.domain.room.entity.RoomDate;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public record RoomResponse(
    String id,
    String title,
    String inviteCode,
    String owner,
    List<LocalDate> dates,
    String startTime,
    String endTime,
    String createdAt,
    String updatedAt,
    String expiresAt
) {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    public RoomResponse(Room room) {
        this(
            String.valueOf(room.getId()),
            room.getTitle(),
            room.getInviteCode(),
            String.valueOf(room.getOwner().getId()),
            room.getDates().stream()
                    .map(RoomDate::getDate)
                    .collect(Collectors.toList()),
            room.getStartTime(),
            room.getEndTime(),
            room.getCreatedAt().toLocalDate().format(DATE_FORMATTER),
            room.getUpdatedAt() == null ? null : room.getUpdatedAt().toLocalDate().format(DATE_FORMATTER),
            room.getExpiresAt().toLocalDate().format(DATE_FORMATTER)
        );
    }
}
