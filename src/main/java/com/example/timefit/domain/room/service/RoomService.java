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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    @Transactional
    public RoomResponse createRoom(RoomCreateRequest request, User owner) {
        String newInviteCode = UUID.randomUUID().toString();

        LocalTime startTime = parseTime(request.getStartTime());
        LocalTime endTime = parseTime(request.getEndTime());

        Room room = new Room(
                request.getTitle(),
                newInviteCode,
                owner,
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
        List<Room> rooms = roomRepository.findAll();

        List<RoomResponse> roomList = new ArrayList<>();

        for (Room room : rooms) {
            RoomResponse dto = new RoomResponse(room);
            roomList.add(dto);
        }

        return roomList;
    }

    @Transactional
    public RoomResponse updateRoom(Long roomId, RoomUpdateRequest dto, User user) {
        Room room = findRoomById(roomId);
        room.validateOwner(user);

        room.updateTitle(dto.getTitle());

        room.updateDates(dto.getDates());

        LocalTime newStartTime = parseTime(dto.getStartTime());
        room.updateStartTime(newStartTime);

        LocalTime newEndTime = parseTime(dto.getEndTime());
        room.updateEndTime(newEndTime);

        return new RoomResponse(room);
    }

    @Transactional
    public RoomDeleteMessage delete(Long roomId, User user) {
        Room room = findRoomById(roomId);
        room.validateOwner(user);
        roomRepository.delete(room);

        return new RoomDeleteMessage("방 삭제 성공");
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
