package com.example.timefit.domain.user.oauth;

import java.util.Map;

public interface OAuth2UserInfo {

    String getProvider();
    String getSocialId();

    Map<String, Object> getAttributes();
}
