package com.example.timefit.domain.room.controller;

import com.example.timefit.domain.room.dto.RoomCreateRequest;
import com.example.timefit.domain.room.dto.RoomDeleteMessage;
import com.example.timefit.domain.room.dto.RoomResponse;
import com.example.timefit.domain.room.dto.RoomUpdateRequest;
import com.example.timefit.domain.room.service.RoomService;
import com.example.timefit.domain.user.entity.User;
import com.example.timefit.domain.user.repository.UserRepository;
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
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<RoomResponse> createRoom(
            @RequestBody RoomCreateRequest request,
            @AuthenticationPrincipal Object principal
    ) {
        User user = findUserByPrincipal(principal);
        RoomResponse responseDTO = roomService.createRoom(request, user);
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
            @RequestBody RoomUpdateRequest request,
            @AuthenticationPrincipal Object principal
    ) {
        User user = findUserByPrincipal(principal);
        RoomResponse responseDTO = roomService.updateRoom(roomId, request, user);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<RoomDeleteMessage> delete(
            @PathVariable Long roomId,
            @AuthenticationPrincipal Object principal
    ) {
        User user = findUserByPrincipal(principal);
        RoomDeleteMessage response = roomService.delete(roomId, user);
        return ResponseEntity.ok(response);
    }

    private User findUserByPrincipal(Object principal) {
        Long userId = Long.parseLong(String.valueOf(principal));

        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. ID: " + userId));
    }
}
