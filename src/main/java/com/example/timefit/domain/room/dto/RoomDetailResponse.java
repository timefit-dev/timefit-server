package com.example.timefit.domain.room.dto;

import java.util.List;

public record RoomDetailResponse(
    Long roomId,
    String title,
    List<String> dates,
    List<String> timeSlots,
    boolean isOwner,
    boolean hasResponded,
    long totalParticipants,
    long respondedCount
) {}
