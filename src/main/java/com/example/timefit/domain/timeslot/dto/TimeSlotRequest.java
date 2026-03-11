package com.example.timefit.domain.timeslot.dto;

import java.time.LocalDateTime;
import java.util.List;

public record TimeSlotRequest(
        List<LocalDateTime> availableSlots
        ) {

}
