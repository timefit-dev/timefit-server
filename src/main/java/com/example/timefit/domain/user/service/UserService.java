package com.example.timefit.domain.user.service;

import org.springframework.stereotype.Service;

import com.example.timefit.domain.user.entity.User;
import com.example.timefit.domain.user.repository.UserRepository;
import com.example.timefit.domain.user.dto.UserUpdateRequest;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException("해당 유저를 찾을 수 없습니다. userId=" + userId)
                );
    }

    @Transactional
    public User updateMyInfo(Long userId, UserUpdateRequest request) {
        User user = getUserById(userId);

        user.updateProfile(
            request.nickname(),
            request.profileImageUrl()
        );

        return user;
    }
}
