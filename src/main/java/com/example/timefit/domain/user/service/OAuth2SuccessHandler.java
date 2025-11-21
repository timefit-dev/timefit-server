package com.example.timefit.domain.user.service;

import com.example.timefit.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    @Value("${oauth2.redirect-url:http://localhost:3000/login/success}")
    private String redirectBaseUrl;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException {

        log.info("[OAuth2SuccessHandler] OAuth2 로그인 성공");

        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();

        Object idAttr = oAuth2User.getAttribute("id");
        if (idAttr == null) {
            throw new IllegalStateException("OAuth2User attributes did not contain id");
        }
        Long userId = Long.valueOf(idAttr.toString());


        log.info("[OAuth2SuccessHandler] 인증된 userId = {}", userId);

        String accessToken = jwtTokenProvider.createAccessToken(userId);
        String refreshToken = jwtTokenProvider.createRefreshToken(userId);

        String redirectUrl = redirectBaseUrl
                + "?access=" + accessToken
                + "&refresh=" + refreshToken;

        log.info("[OAuth2SuccessHandler] Redirect URL = {}", redirectUrl);

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}
