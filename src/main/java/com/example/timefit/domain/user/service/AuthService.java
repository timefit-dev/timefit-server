package com.example.timefit.domain.user.service;

import org.springframework.stereotype.Service;

import com.example.timefit.domain.user.dto.AuthRequest;
import com.example.timefit.domain.user.dto.AuthResponse;

@Service
public class AuthService {
  
  public AuthResponse socialLogin(AuthRequest request) {
    //카카오인가코드 받아오는 로직 추가
    System.out.println("provider: " + request.provider());
    System.out.println("authorizationCode: " + request.authorizationCode());
    return null;
  }
}
