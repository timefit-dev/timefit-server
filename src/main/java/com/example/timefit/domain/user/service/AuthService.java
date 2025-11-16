package com.example.timefit.domain.user.service;

import com.example.timefit.domain.user.entity.User;
import com.example.timefit.domain.user.repository.UserRepository;
import com.example.timefit.domain.user.oauth.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String provider = userRequest.getClientRegistration().getRegistrationId();
        
        OAuth2UserInfo userInfo = switch (provider) {
            case "kakao" -> new KakaoOAuth2UserInfo(attributes);
            // case "google" -> new GoogleOAuth2UserInfo(attributes);
            // case "apple" -> new AppleOAuth2UserInfo(attributes);
            default -> throw new IllegalArgumentException("지원하지 않는 provider: " + provider);
        };

        User user = userRepository.findBySocialId(userInfo.getSocialId())
                .orElseGet(() -> {
                    User newUser = new User(
                            userInfo.getSocialId(),
                            userInfo.getProvider(),
                            userInfo.getNickname(),
                            userInfo.getProfileImage()
                    );
                    return userRepository.save(newUser);
                });

        log.info("로그인 성공, provider={}, userId={}", provider, user.getId());

        return new DefaultOAuth2User(
                List.of(new SimpleGrantedAuthority("ROLE_USER")),
                attributes,
                "id"
        );
    }
}
