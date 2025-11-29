package com.example.timefit.domain.user.service;

import com.example.timefit.domain.user.oauth.KakaoOAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoOAuthService {

    private final RestTemplate restTemplate = new RestTemplate();

    public KakaoOAuth2UserInfo getUserInfo(String accessToken) {

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Map<String, Object>> response =
                restTemplate.exchange(
                        "https://kapi.kakao.com/v2/user/me",
                        HttpMethod.GET,
                        entity,
                        new ParameterizedTypeReference<Map<String, Object>>() {}
                );

        log.info("[KakaoOAuthService] 카카오 사용자 정보 조회 성공");

        return new KakaoOAuth2UserInfo(response.getBody());
    }
}
