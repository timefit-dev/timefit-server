package com.example.timefit.domain.user.service;

import com.example.timefit.domain.user.dto.LoginResponse;

public interface SocialLoginService {

  String getProviderName();

  LoginResponse login(String authorizationCode);
  
}
