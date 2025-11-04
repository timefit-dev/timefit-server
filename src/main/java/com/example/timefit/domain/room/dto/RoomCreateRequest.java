package com.example.timefit.domain.room.dto;

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
