package com.example.timefit.domain.user.oauth;

import java.util.Map;

public class GoogleOAuth2UserInfo implements OAuth2UserInfo {

    private final Map<String, Object> attributes;

    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProvider() {
        return "GOOGLE";
    }

    @Override
    public String getSocialId() {
        return attributes.get("sub").toString();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }
}
