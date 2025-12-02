package com.example.timefit.domain.user.oauth;

import java.util.Map;

public class KakaoOAuth2UserInfo implements OAuth2UserInfo {
    private final Map<String, Object> attributes;

    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProvider() {
        return "KAKAO";
    }

    @Override
    public String getSocialId() {
        return attributes.get("id").toString();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }
}
