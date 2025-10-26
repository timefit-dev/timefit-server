package com.example.timefit.domain.room.dto;

import com.example.timefit.domain.room.entity.Room;
import lombok.Getter;

public class RoomResponse {
    @Getter
    public static class CreateDTO {
        private final Long roomId;
        private final String inviteCode;

        public CreateDTO(Room room) {
            this.roomId = room.getId();
            this.inviteCode = room.getInviteCode();
        }
    }
}
