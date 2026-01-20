package com.example.timefit.domain.room.dto;

import com.example.timefit.domain.room.entity.Room;
import com.example.timefit.domain.room.entity.RoomDate;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public record RoomResponse(
        String id,
        String title,
        String inviteCode,
        String owner,
        List<LocalDate> dates,
        LocalTime startTime,
        LocalTime endTime,
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
                room.getStartTime(),
                room.getEndTime(),
                generateTimeList(room.getStartTime(), room.getEndTime()),
                room.getCreatedAt().toLocalDate().format(DATE_FORMATTER),
                room.getUpdatedAt() == null ? null : room.getUpdatedAt().toLocalDate().format(DATE_FORMATTER),
                room.getExpiresAt().toLocalDate().format(DATE_FORMATTER)
        );
    }

    private static List<String> generateTimeList(LocalTime startTime, LocalTime endTime) {
        List<String> timelist = new ArrayList<>();

        if (startTime.isAfter(endTime)) {
            return timelist;
        }

        LocalTime current = startTime;

        int safetyCounter = 0;

        while (!current.isAfter(endTime)) {
            timelist.add(current.format(TIME_FORMATTER));

            LocalTime next = current.plusHours(1);

            if (next.isBefore(current)) {
                timelist.add("24:00");
                break;
            }

            if (safetyCounter++ > 24) {
                timelist.add("24:00");
                break;
            }

            current = next;
        }

        return timelist;
    }
}
