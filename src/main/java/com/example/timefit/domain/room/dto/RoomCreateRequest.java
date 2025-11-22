package com.example.timefit.domain.room.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class RoomCreateRequest {
    private String title;
    private List<LocalDate> dates;
    private String startTime;
    private String endTime;
}
