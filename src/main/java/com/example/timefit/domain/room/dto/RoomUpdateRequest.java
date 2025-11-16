package com.example.timefit.domain.room.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class RoomUpdateRequest {
    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotEmpty(message = "날짜를 하나 이상 선택해주세요.")
    private List<LocalDate> dates;

    @NotBlank(message = "시작 시간을 입력해주세요.")
    private String startTime;

    @NotBlank(message = "종료 시간을 입력해주세요.")
    private String endTime;
}
