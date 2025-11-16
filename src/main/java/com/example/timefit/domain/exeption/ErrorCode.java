package com.example.timefit.domain.exeption;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 방을 찾을 수 없습니다. id:"),
    OWNER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String message;
}
