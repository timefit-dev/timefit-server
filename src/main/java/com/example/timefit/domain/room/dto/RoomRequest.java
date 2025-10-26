package com.example.timefit.domain.room.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class RoomRequest {
    @Getter
    @NoArgsConstructor
    public static class CreateDTO{
        private String title;
        private List<LocalDate> dates;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        private LocalTime startTime;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        private LocalTime endTime;
    }
}
