package com.example.timefit.domain.room.controller;

import com.example.timefit.domain.room.dto.MessageResponse;
import com.example.timefit.domain.room.dto.RoomRequest;
import com.example.timefit.domain.room.dto.RoomResponse;
import com.example.timefit.domain.room.dto.RoomUpdateRequest;
import com.example.timefit.domain.room.service.RoomService;
import com.example.timefit.domain.user.entity.User;
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
            @RequestBody RoomRequest requestDTO,
            @AuthenticationPrincipal User user
    ) {
        RoomResponse responseDTO = roomService.createRoom(requestDTO, user);

        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<RoomResponse>> getAllRooms() {
        List<RoomResponse> responseList = roomService.getAllRooms();
        return ResponseEntity.ok(responseList);
    }

    @PutMapping("/{roomId}")
    public ResponseEntity<RoomResponse> updateRoom(
            @PathVariable Long roomId,
            @RequestBody RoomUpdateRequest requestDTO,
            @AuthenticationPrincipal User user
    ) {
        RoomResponse responseDTO = roomService.updateRoom(roomId, requestDTO, user);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<MessageResponse> deleteRoom(
            @PathVariable Long roomId,
            @AuthenticationPrincipal User user
    ) {
        MessageResponse response = roomService.deleteRoom(roomId, user);
        return ResponseEntity.ok(response);
    }
}
