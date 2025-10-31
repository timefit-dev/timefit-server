package com.example.timefit.domain.room.dto;

import com.example.timefit.domain.room.entity.Room;
import lombok.Getter;

public class RoomResponse {
    public record CreateDTO(Long roomId, String inviteCode) {
        public CreateDTO(Room room) {
            this(room.getId(), room.getInviteCode());
        }
    }
}
