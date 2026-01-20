package com.example.timefit.domain.room.service;

import com.example.timefit.domain.exeption.CustomException;
import com.example.timefit.domain.exeption.ErrorCode;
import com.example.timefit.domain.room.dto.RoomCreateRequest;
import com.example.timefit.domain.room.dto.RoomDeleteMessage;
import com.example.timefit.domain.room.dto.RoomResponse;
import com.example.timefit.domain.room.dto.RoomUpdateRequest;
import com.example.timefit.domain.room.entity.Room;
import com.example.timefit.domain.room.entity.RoomDate;
import com.example.timefit.domain.room.repository.RoomRepository;
import com.example.timefit.domain.user.entity.User;
import com.example.timefit.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    @Transactional
    public RoomResponse createRoom(RoomCreateRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("없는 유저입니다."));

        String newInviteCode = UUID.randomUUID().toString();

        LocalTime startTime = parseTime(request.getStartTime());
        LocalTime endTime = parseTime(request.getEndTime());

        Room room = new Room(
                request.getTitle(),
                newInviteCode,
                user,
                startTime,
                endTime
        );
        room.onPrePersist();

        for (LocalDate date : request.getDates()) {
            room.getDates().add(new RoomDate(date, room));
        }

        Room savedRoom = roomRepository.save(room);

        return new RoomResponse(savedRoom);
    }

    @Transactional(readOnly = true)
    public List<RoomResponse> getAllRooms() {
        return roomRepository.findAll().stream()
                .map(RoomResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public RoomResponse updateRoom(Long roomId, RoomUpdateRequest request, Long userId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 방입니다."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("없는 유저입니다."));

        room.validateOwner(user);

        room.updateTitle(request.getTitle());

        room.updateDates(request.getDates());

        LocalTime newStartTime = parseTime(request.getStartTime());
        room.updateStartTime(newStartTime);

        LocalTime newEndTime = parseTime(request.getEndTime());
        room.updateEndTime(newEndTime);

        return new RoomResponse(room);
    }

    @Transactional
    public RoomDeleteMessage delete(Long roomId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("없는 유저입니다."));

        Room room = findRoomById(roomId);
        room.validateOwner(user);
        roomRepository.delete(room);

        return new RoomDeleteMessage("방 삭제 성공");
    }

    @Transactional(readOnly = true)
    public RoomResponse getRoomByInviteCode(String inviteCode, Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("없는 유저입니다."));

        Room room = roomRepository.findByInviteCode(inviteCode);

        if (room == null) {
            throw new CustomException(ErrorCode.ROOM_NOT_FOUND);
        }

        return new RoomResponse(room);
    }

    private Room findRoomById(Long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.ROOM_NOT_FOUND));
    }

    private LocalTime parseTime(String timeString) {
        if (timeString == null || timeString.isBlank()) {
            return null;
        }
        if ("24:00".equals(timeString)) {
            return LocalTime.of(23, 59);
        }
        return LocalTime.parse(timeString, DateTimeFormatter.ofPattern("HH:mm"));
    }
}
