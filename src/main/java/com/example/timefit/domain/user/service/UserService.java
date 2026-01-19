package com.example.timefit.domain.user.service;

import org.springframework.stereotype.Service;

import com.example.timefit.domain.user.entity.User;
import com.example.timefit.domain.user.repository.UserRepository;
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
    public User updateNickname(Long userId, String nickname) {
        User user = getUserById(userId);
        user.updateNickname(nickname);
        return user;
    }

    @Transactional
    public User updateProfileImage(Long userId, String profileImageUrl) {
        User user = getUserById(userId);
        user.updateProfileImage(profileImageUrl);
        return user;
    }
}
