package com.example.timefit.domain.user.controller;

import com.example.timefit.domain.user.dto.AuthRequest;
import com.example.timefit.domain.user.dto.AuthResponse;
import com.example.timefit.domain.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
  
  private final AuthService authService;

  @PostMapping("/login")
  public AuthResponse socialLogin(@RequestBody AuthRequest request) {
    return authService.socialLogin(request);
  }
}
