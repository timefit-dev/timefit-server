package com.example.timefit.domain.user.service;

import com.example.timefit.domain.user.entity.User;
import com.example.timefit.domain.user.entity.RefreshToken;
import com.example.timefit.domain.user.repository.UserRepository;
import com.example.timefit.domain.user.repository.RefreshTokenRepository;
import com.example.timefit.domain.user.oauth.AppleOAuth2UserInfo;
import com.example.timefit.domain.user.oauth.KakaoOAuth2UserInfo;
import com.example.timefit.domain.user.oauth.GoogleOAuth2UserInfo;
import com.example.timefit.global.jwt.JwtTokenProvider;
import com.example.timefit.domain.user.dto.LoginResponse;
import com.example.timefit.domain.user.dto.UserResponse;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final KakaoOAuthService kakaoOAuthService;
    private final GoogleOAuthService googleOAuthService;
    private final AppleOAuthService appleOAuthService;

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginResponse socialLogin(String provider, String accessToken) {
        return switch (provider.toUpperCase()) {
            case "KAKAO" -> loginWithKakao(accessToken);
            case "GOOGLE" -> loginWithGoogle(accessToken);
            case "APPLE" -> loginWithApple(accessToken);
            default -> throw new IllegalArgumentException("Unsupported provider: " + provider);
        };
    }

    public LoginResponse reissueAccessToken(String refreshToken) {

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        RefreshToken savedToken = refreshTokenRepository
                .findByToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Refresh token not found"));

        Long userId = savedToken.getUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String newAccessToken = jwtTokenProvider.createAccessToken(userId);

        return new LoginResponse(
                newAccessToken,
                refreshToken,
                new UserResponse(user)
        );
    }

    @Transactional
    public void logout(String accessToken) {

        if(!jwtTokenProvider.validateToken(accessToken)) {
                throw new IllegalArgumentException("Invalid access token");
        }

        Long userId = jwtTokenProvider.getUserId(accessToken);

        refreshTokenRepository.deleteByUserId(userId);

        log.info("[AuthService] 로그아웃 완료 - userId={}", userId);
    }

    private LoginResponse loginWithKakao(String kakaoAccessToken) {

        log.info("[AuthService] loginWithKakao start");

        KakaoOAuth2UserInfo userInfo =
                kakaoOAuthService.getUserInfo(kakaoAccessToken);
        
        User user = userRepository.findBySocialId(userInfo.getSocialId())
                .orElseGet(() -> userRepository.save(
                        new User(
                                userInfo.getSocialId(),
                                userInfo.getProvider(),
                                userInfo.getNickname()
                        )
                ));

        String accessToken = jwtTokenProvider.createAccessToken(user.getId());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId());

        saveRefreshToken(user.getId(), refreshToken);

        return new LoginResponse(
                accessToken,
                refreshToken,
                new UserResponse(user)
        );
    }

    private LoginResponse loginWithGoogle(String googleAccessToken) {

        GoogleOAuth2UserInfo userInfo =
                googleOAuthService.getUserInfo(googleAccessToken);

        User user = userRepository.findBySocialId(userInfo.getSocialId())
                .orElseGet(() -> userRepository.save(
                        new User(
                                userInfo.getSocialId(),
                                userInfo.getProvider(),
                                userInfo.getNickname()
                        )
                ));

        String accessToken = jwtTokenProvider.createAccessToken(user.getId());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId());

        saveRefreshToken(user.getId(), refreshToken);

        return new LoginResponse(
                accessToken,
                refreshToken,
                new UserResponse(user)
        );
    }

    private LoginResponse loginWithApple(String identityToken) {

        AppleOAuth2UserInfo userInfo =
                appleOAuthService.getUserInfo(identityToken);

        User user = userRepository.findBySocialId(userInfo.getSocialId())
                .orElseGet(() -> userRepository.save(
                        new User(
                                userInfo.getSocialId(),
                                userInfo.getProvider(),
                                generateDefaultNickname()
                        )
                ));

        String accessToken = jwtTokenProvider.createAccessToken(user.getId());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId());

        saveRefreshToken(user.getId(), refreshToken);

        return new LoginResponse(
                accessToken,
                refreshToken,
                new UserResponse(user)
        );
    }

    private String generateDefaultNickname() {
        return "사용자_" + UUID.randomUUID().toString().substring(0, 8);
    }

    private void saveRefreshToken(Long userId, String refreshToken) {

        LocalDateTime expiry = LocalDateTime.now().plusDays(14);

        refreshTokenRepository.findByUserId(userId)
                .ifPresentOrElse(
                        rt -> rt.update(refreshToken, expiry),
                        () -> refreshTokenRepository.save(
                                RefreshToken.create(userId, refreshToken, expiry)
                        )
                );
    }
}
