package com.example.timefit.domain.room.dto;

import com.example.timefit.domain.room.entity.Room;
import com.example.timefit.domain.room.entity.RoomDate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public record RoomResponse(
        String id,
        String title,
        String invitecode,
        String owner,
        List<LocalDate> dates,
        List<String> timelist,
        String createdAt,
        String updatedAt,
        String expiresAt
) {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public RoomResponse(Room room) {
        this(
                String.valueOf(room.getId()),
                room.getTitle(),
                room.getInviteCode(),
                String.valueOf(room.getOwner().getId()),
                room.getDates().stream()
                        .map(RoomDate::getDate)
                        .collect(Collectors.toList()),
                generateTimeList(room.getStartTime(), room.getEndTime()),
                room.getCreatedAt().toLocalDate().format(DATE_FORMATTER),
                room.getUpdatedAt() == null ? null : room.getUpdatedAt().toLocalDate().format(DATE_FORMATTER),
                room.getExpiresAt().toLocalDate().format(DATE_FORMATTER)
        );
    }

    private static List<String> generateTimeList(LocalTime startTime, LocalTime endTime) {
        List<String> timelist = new ArrayList<>();

        LocalTime current = startTime;
        while (!current.isAfter(endTime)) {
            timelist.add(current.format(TIME_FORMATTER));
            current = current.plusHours(1);
        }

        return timelist;
    }
}
