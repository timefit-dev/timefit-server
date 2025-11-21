package com.example.timefit.domain.user.controller;

import com.example.timefit.domain.user.dto.UserResponse;
import com.example.timefit.domain.user.entity.User;
import com.example.timefit.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

  private final UserRepository userRepository;

  @GetMapping("/me")
    public ResponseEntity<UserResponse> getMyInfo(@AuthenticationPrincipal OAuth2User oAuth2User) {

        Object idAttr = oAuth2User.getAttribute("id");
        if (idAttr == null) {
            throw new IllegalStateException("OAuth2User attributes did not contain id");
        }

        String socialId = idAttr.toString();

        User user = userRepository.findBySocialId(socialId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다."));

        return ResponseEntity.ok(new UserResponse(user));
    }
}
