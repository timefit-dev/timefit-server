package com.example.timefit.domain.user.service;

import com.example.timefit.domain.user.oauth.AppleOAuth2UserInfo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppleOAuthService {

    private static final String APPLE_KEYS_URL = "https://appleid.apple.com/auth/keys";

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AppleOAuth2UserInfo getUserInfo(String identityToken) {
        try {
            String kid = extractKidFromToken(identityToken);
            PublicKey publicKey = getApplePublicKey(kid);

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(identityToken)
                    .getBody();

            Map<String, Object> attributes = new HashMap<>(claims);

            log.info("[AppleOAuthService] 애플 사용자 정보 조회 성공");

            return new AppleOAuth2UserInfo(attributes);
        } catch (Exception e) {
            log.error("[AppleOAuthService] Identity Token 검증 실패", e);
            throw new IllegalArgumentException("Invalid Apple Identity Token", e);
        }
    }

    private String extractKidFromToken(String token) throws Exception {
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid JWT format");
        }

        String header = new String(Base64.getUrlDecoder().decode(parts[0]));
        JsonNode headerNode = objectMapper.readTree(header);

        return headerNode.get("kid").asText();
    }

    private PublicKey getApplePublicKey(String kid) throws Exception {
        String response = restTemplate.getForObject(APPLE_KEYS_URL, String.class);
        JsonNode keys = objectMapper.readTree(response).get("keys");

        for (JsonNode key : keys) {
            if (kid.equals(key.get("kid").asText())) {
                String n = key.get("n").asText();
                String e = key.get("e").asText();

                byte[] nBytes = Base64.getUrlDecoder().decode(n);
                byte[] eBytes = Base64.getUrlDecoder().decode(e);

                BigInteger modulus = new BigInteger(1, nBytes);
                BigInteger exponent = new BigInteger(1, eBytes);

                RSAPublicKeySpec spec = new RSAPublicKeySpec(modulus, exponent);
                KeyFactory factory = KeyFactory.getInstance("RSA");

                return factory.generatePublic(spec);
            }
        }

        throw new IllegalArgumentException("Apple public key not found for kid: " + kid);
    }
}
