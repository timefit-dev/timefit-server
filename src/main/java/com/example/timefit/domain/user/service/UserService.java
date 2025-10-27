package com.example.timefit.domain.user.service;

import com.example.timefit.domain.user.dto.UserResponse;
import com.example.timefit.domain.user.entity.User;
import com.example.timefit.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;


@Service
public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
      this.userRepository = userRepository;
  }

  public UserResponse getUserBySocialId(String socialId) {
    User user = userRepository.findBySocialId(socialId)
            .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다. socialId =" + socialId));
    return new UserResponse(user);
  }
  
}
