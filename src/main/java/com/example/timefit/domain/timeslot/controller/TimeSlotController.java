package com.example.timefit.domain.timeslot.controller;

import com.example.timefit.domain.timeslot.dto.TimeSlotRequest;
import com.example.timefit.domain.timeslot.dto.TimeSlotResultsResponse;
import com.example.timefit.domain.timeslot.dto.TimeSlotDetailResponse;
import com.example.timefit.domain.timeslot.service.TimeSlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rooms")
public class TimeSlotController {

    private final TimeSlotService timeSlotService;

    @PostMapping("/{roomId}/response")
    public ResponseEntity<TimeSlotResultsResponse> submitTimeSlots(
            @PathVariable Long roomId,
            @AuthenticationPrincipal Long userId,
            @RequestBody TimeSlotRequest request
    ) {

        TimeSlotResultsResponse response
                = timeSlotService.submit(roomId, userId, request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{roomId}/results")
    public ResponseEntity<TimeSlotResultsResponse> getResults(
            @PathVariable Long roomId,
            @AuthenticationPrincipal Long userId
    ) {
        return ResponseEntity.ok(timeSlotService.getResults(roomId, userId));
    }

    @GetMapping("/{roomId}/results/detail")
    public ResponseEntity<TimeSlotDetailResponse> getResultDetail(
            @PathVariable Long roomId,
            @AuthenticationPrincipal Long userId,
            @RequestParam String dateTime // "2026-02-10T13:00"
    ) {
        return ResponseEntity.ok(timeSlotService.getResultDetail(roomId, userId, dateTime));
    }
}
