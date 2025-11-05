package com.example.timefit.domain.room.service;

import com.example.timefit.domain.room.dto.RoomCreateRequest;
import com.example.timefit.domain.room.dto.RoomDeleteMessage;
import com.example.timefit.domain.room.dto.RoomResponse;
import com.example.timefit.domain.room.dto.RoomUpdateRequest;
import com.example.timefit.domain.room.entity.Room;
import com.example.timefit.domain.room.entity.RoomDate;
import com.example.timefit.domain.room.repository.RoomRepository;
import com.example.timefit.domain.user.entity.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    @Transactional
    public RoomResponse createRoom(RoomCreateRequest dto, User owner) {
        String newInviteCode = UUID.randomUUID().toString();

        LocalTime startTime = parseTime(dto.getStartTime());
        LocalTime endTime = parseTime(dto.getEndTime());

        Room room = new Room(
                dto.getTitle(),
                newInviteCode,
                owner,
                startTime,
                endTime
        );
        room.onPrePersist();

        for (LocalDate date : dto.getDates()) {
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
    public RoomResponse updateRoom(Long roomId, RoomUpdateRequest dto, User user) {
        Room room = findRoomById(roomId);
        checkRoomOwner(room, user);

        if (dto.getTitle() != null && !dto.getTitle().isBlank()) {
            room.updateTitle(dto.getTitle());
        }

        if (dto.getDates() != null) {
            room.updateDates(dto.getDates());
        }

        if (dto.getStartTime() != null) {
            LocalTime newStartTime = parseTime(dto.getStartTime());
            room.updateStartTime(newStartTime);
        }

        if (dto.getEndTime() != null) {
            LocalTime newEndTime = parseTime(dto.getEndTime());
            room.updateEndTime(newEndTime);
        }

        return new RoomResponse(room);
    }

    @Transactional
    public RoomDeleteMessage deleteRoom(Long roomId, User user) {
        Room room = findRoomById(roomId);
        checkRoomOwner(room, user);
        roomRepository.delete(room);

        return new RoomDeleteMessage("방 삭제 성공");
    }


    private Room findRoomById(Long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("해당 방을 찾을 수 없습니다. id: " + roomId));
    }

    private void checkRoomOwner(Room room, User user) {
        if (!room.getOwner().getId().equals(user.getId())) {
            throw new RuntimeException("방을 수정/삭제할 권한이 없습니다.");
        }
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
