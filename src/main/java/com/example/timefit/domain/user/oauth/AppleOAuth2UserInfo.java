package com.example.timefit.domain.user.oauth;

import java.util.Map;

public class AppleOAuth2UserInfo implements OAuth2UserInfo {

    private final Map<String, Object> attributes;

    public AppleOAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProvider() {
        return "APPLE";
    }

    @Override
    public String getSocialId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getNickname() {
        return null;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public String getEmail() {
        return (String) attributes.get("email");
    }

    public Boolean isEmailVerified() {
        Object emailVerified = attributes.get("email_verified");
        if (emailVerified instanceof Boolean) {
            return (Boolean) emailVerified;
        }
        if (emailVerified instanceof String) {
            return Boolean.parseBoolean((String) emailVerified);
        }
        return false;
    }
}
