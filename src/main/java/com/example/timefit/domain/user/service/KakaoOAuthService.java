package com.example.timefit.domain.user.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.example.timefit.domain.user.dto.LoginResponse;
import com.example.timefit.domain.user.dto.UserResponse;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class KakaoOAuthService implements SocialLoginService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${oauth.kakao.client-id}")
    private String clientId;

    @Value("${oauth.kakao.redirect-uri}")
    private String redirectUri;

    @Value("${oauth.kakao.client-secret:}")
    private String clientSecret;

    @Override
    public String getProviderName() {
        return "kakao";
    }

    @Override
    public LoginResponse login(String authorizationCode) {
        log.info("[KakaoOAuthService] 인가코드로 카카오 로그인 진행 - code: {}", authorizationCode);
        
        String tokenUrl = "https://kauth.kakao.com/oauth/token";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", authorizationCode);
        if (clientSecret != null && !clientSecret.isEmpty()) {
            params.add("client_secret", clientSecret);
        }

        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(params, headers);
        ResponseEntity<Map<String, Object>> tokenResponse = restTemplate.exchange(
                tokenUrl,
                HttpMethod.POST,
                tokenRequest,
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        Map<String, Object> tokenBody = tokenResponse.getBody();
        if (!tokenResponse.getStatusCode().is2xxSuccessful() || tokenBody == null) {
            throw new IllegalStateException("카카오 토큰 요청 실패: " + tokenResponse.getStatusCode());
        }

        String accessToken = (String) tokenBody.get("access_token");
        String refreshToken = (String) tokenBody.get("refresh_token");
        log.info("[KakaoOAuthService] Token 발급 완료 (access={}, refresh={})", accessToken, refreshToken);

        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";
        HttpHeaders userHeaders = new HttpHeaders();
        userHeaders.setBearerAuth(accessToken);
        HttpEntity<Void> userRequest = new HttpEntity<>(userHeaders);

        ResponseEntity<Map<String, Object>> userResponse = restTemplate.exchange(
                userInfoUrl,
                HttpMethod.GET,
                userRequest,
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        Map<String, Object> responseBody = userResponse.getBody();
        if (!userResponse.getStatusCode().is2xxSuccessful() || responseBody == null) {
            throw new IllegalStateException("카카오 사용자 정보 요청 실패: " + userResponse.getStatusCode());
        }

        Long kakaoId = responseBody.get("id") != null ? ((Number) responseBody.get("id")).longValue() : null;
        String socialId = kakaoId != null ? "kakao_" + kakaoId : "kakao_unknown";

        Map<String, Object> properties = safeCast(responseBody.get("properties"));
        String nickname = properties != null ? (String) properties.getOrDefault("nickname", "카카오 사용자") : "카카오 사용자";
        String profileImage = properties != null ? (String) properties.getOrDefault("profile_image", null) : null;

        log.info("[KakaoOAuthService] 사용자 닉네임: {}, socialId: {}", nickname, socialId);

        UserResponse user = new UserResponse(null, socialId, nickname, profileImage, getProviderName());
        return new LoginResponse(accessToken, refreshToken, user);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> safeCast(Object obj) {
        if (obj instanceof Map<?, ?> map) {
            return (Map<String, Object>) map;
        }
        return null;
    }
}
