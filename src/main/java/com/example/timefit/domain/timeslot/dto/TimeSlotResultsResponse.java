package com.example.timefit.domain.timeslot.dto;

import java.util.List;

public record TimeSlotResultsResponse(
        Long roomId,
        long totalParticipants,
        long respondedCount,
        List<String> bestSlots,
        List<String> lastSlots,
        List<SlotCount> slotCounts
        ) {

    public record SlotCount(
            String dateTime,
            int count
            ) {

    }
}
