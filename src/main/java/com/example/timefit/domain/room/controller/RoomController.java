package com.example.timefit.domain.room.controller;

import com.example.timefit.domain.room.dto.RoomCreateRequest;
import com.example.timefit.domain.room.dto.RoomDeleteMessage;
import com.example.timefit.domain.room.dto.RoomResponse;
import com.example.timefit.domain.room.dto.RoomUpdateRequest;
import com.example.timefit.domain.room.service.RoomService;
import com.example.timefit.domain.room.dto.RoomDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<RoomResponse> createRoom(
            @RequestBody RoomCreateRequest request,
            @AuthenticationPrincipal Long userId
    ) {
        RoomResponse response = roomService.createRoom(request, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<RoomResponse>> getAllRooms() {
        List<RoomResponse> responseList = roomService.getAllRooms();
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/invite/{inviteCode}")
    public ResponseEntity<RoomResponse> getRoomByInviteCode(
            @PathVariable String inviteCode,
            @AuthenticationPrincipal Long userId
    ) {
        RoomResponse response = roomService.getRoomByInviteCode(inviteCode, userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{roomId}")
    public ResponseEntity<RoomResponse> updateRoom(
            @PathVariable Long roomId,
            @RequestBody RoomUpdateRequest request,
            @AuthenticationPrincipal Long userId
    ) {
        RoomResponse responseDTO = roomService.updateRoom(roomId, request, userId);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<RoomDeleteMessage> delete(
            @PathVariable Long roomId,
            @AuthenticationPrincipal Long userId
    ) {
        RoomDeleteMessage response = roomService.delete(roomId, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<RoomDetailResponse> getRoomDetail(
        @PathVariable Long roomId,
        @AuthenticationPrincipal Long userId
    ) {
        RoomDetailResponse response = roomService.getRoomDetail(roomId, userId);
        return ResponseEntity.ok(response);
    }
}
