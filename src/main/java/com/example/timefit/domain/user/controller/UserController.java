package com.example.timefit.domain.user.controller;

import com.example.timefit.domain.user.dto.UserResponse;
import com.example.timefit.domain.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
      this.userService = userService;
  }

  @GetMapping("/{socialId}")
  public ResponseEntity<UserResponse> getUserInfo(@PathVariable String socialId) {
    UserResponse user = userService.getUserBySocialId(socialId);
    return ResponseEntity.ok(user);
  }
}
