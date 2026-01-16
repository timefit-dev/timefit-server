package com.example.timefit.domain.user.controller;

import com.example.timefit.domain.user.dto.UserResponse;
import com.example.timefit.domain.user.dto.UserUpdateRequest;
import com.example.timefit.domain.user.entity.User;
import com.example.timefit.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMyInfo(Authentication authentication) {

        Long userId = Long.valueOf(authentication.getName());

        User user = userService.getUserById(userId);

        return ResponseEntity.ok(new UserResponse(user));
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateMyInfo(
        Authentication authentication,
        @RequestBody UserUpdateRequest request
    ) {
        Long userId = Long.valueOf(authentication.getName());

        User updatedUser = userService.updateMyInfo(userId, request);

        return ResponseEntity.ok(new UserResponse(updatedUser));
    }
}
