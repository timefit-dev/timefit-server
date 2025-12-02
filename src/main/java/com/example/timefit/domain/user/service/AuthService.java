package com.example.timefit.domain.user.service;

import com.example.timefit.domain.user.entity.User;
import com.example.timefit.domain.user.entity.RefreshToken;
import com.example.timefit.domain.user.repository.UserRepository;
import com.example.timefit.domain.user.repository.RefreshTokenRepository;
import com.example.timefit.domain.user.oauth.KakaoOAuth2UserInfo;
import com.example.timefit.global.jwt.JwtTokenProvider;
import com.example.timefit.domain.user.dto.LoginResponse;
import com.example.timefit.domain.user.dto.UserResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final KakaoOAuthService kakaoOAuthService;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginResponse socialLogin(String provider, String accessToken) {

        switch (provider.toUpperCase()) {
            case "KAKAO":
                return loginWithKakao(accessToken);

            default:
                throw new IllegalArgumentException("Unsupported provider: " + provider);
        }
    }

    private LoginResponse loginWithKakao(String kakaoAccessToken) {

        KakaoOAuth2UserInfo userInfo = kakaoOAuthService.getUserInfo(kakaoAccessToken);

        User user = userRepository.findBySocialId(userInfo.getSocialId())
                .orElseGet(() -> userRepository.save(
                        new User(
                                userInfo.getSocialId(),
                                "KAKAO"
                        )
                ));

        String accessToken = jwtTokenProvider.createAccessToken(user.getId());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId());

        saveRefreshToken(user.getId(), refreshToken);

        return new LoginResponse(accessToken, refreshToken, new UserResponse(user));
    }

    @SuppressWarnings("null")
    private void saveRefreshToken(Long userId, String refreshToken) {

        LocalDateTime expiry = LocalDateTime.now().plusDays(14);

        refreshTokenRepository.findByUserId(userId)
                .ifPresentOrElse(
                        rt -> rt.update(refreshToken, expiry),
                        () -> refreshTokenRepository.save(
                                RefreshToken.create(
                                        userId,
                                        refreshToken,
                                        expiry
                                )
                        )
                );
    }
}
