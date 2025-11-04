package com.example.timefit.domain.room.service;

import com.example.timefit.domain.room.dto.MessageResponse;
import com.example.timefit.domain.room.dto.RoomRequest;
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
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    @Transactional
    public RoomResponse createRoom(RoomRequest dto, User owner) {
        String newInviteCode = UUID.randomUUID().toString().substring(0, 10);

        Room room = new Room(
                dto.getTitle(),
                newInviteCode,
                owner,
                dto.getStartTime(),
                dto.getEndTime()
        );

        room.onPrePersist();

        for (LocalDate date : dto.getDates()) {
            RoomDate roomDate = new RoomDate(date, room);
            room.getDates().add(roomDate);
        }

        Room savedRoom = roomRepository.save(room);

        return new RoomResponse(savedRoom);
    }

    @Transactional(readOnly = true)
    public List<RoomResponse> getAllRooms() {
        List<Room> rooms = roomRepository.findAll();

        return rooms.stream()
                .map(RoomResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public RoomResponse updateRoom(Long roomId, RoomUpdateRequest dto, User user) {
        Room room = findRoomById(roomId);

        checkRoomOwner(room, user);

        room.patchUpdate(dto);

        return new RoomResponse(room);
    }

    @Transactional
    public MessageResponse deleteRoom(Long roomId, User user) {
        Room room = findRoomById(roomId);

        checkRoomOwner(room, user);

        roomRepository.delete(room);

        return new MessageResponse("방 삭제 성공");
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
}
