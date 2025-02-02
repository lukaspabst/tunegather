package com.soundsync.main.service;

import com.soundsync.main.Config.SpotifyConfig;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.StringUtils;
import org.json.JSONObject;

@Service
public class SpotifyAuthService {

    private final SpotifyConfig spotifyConfig;
    private String accessToken;
    private String refreshToken;
    private long tokenExpirationTime;

    public SpotifyAuthService(SpotifyConfig spotifyConfig) {
        this.spotifyConfig = spotifyConfig;
    }

    public String getAccessToken() {
        if (isTokenExpired()) {
            if (StringUtils.hasText(refreshToken)) {
                refreshAccessToken();
            } else {
                retrieveNewAccessToken();
            }
        }
        return accessToken;
    }

    private boolean isTokenExpired() {
        return System.currentTimeMillis() > tokenExpirationTime;
    }

    private void retrieveNewAccessToken() {
        String url = "https://accounts.spotify.com/api/token";
        String body = "grant_type=client_credentials";

        HttpEntity<String> entity = createTokenRequestEntity(body);
        ResponseEntity<String> response = executeTokenRequest(url, entity);

        if (response != null) {
            parseTokenResponse(response.getBody());
            this.tokenExpirationTime = System.currentTimeMillis() + 3600000;
        }
    }

    private void refreshAccessToken() {
        if (StringUtils.hasText(refreshToken)) {
            String url = "https://accounts.spotify.com/api/token";
            String body = "grant_type=refresh_token&refresh_token=" + refreshToken;

            HttpEntity<String> entity = createTokenRequestEntity(body);
            ResponseEntity<String> response = executeTokenRequest(url, entity);

            if (response != null) {
                parseTokenResponse(response.getBody());
                this.tokenExpirationTime = System.currentTimeMillis() + 3600000;
            }
        }
    }
    private HttpEntity<String> createTokenRequestEntity(String body) {
        String credentials = spotifyConfig.getClientId() + ":" + spotifyConfig.getClientSecret();
        String base64EncodedCredentials = java.util.Base64.getEncoder().encodeToString(credentials.getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64EncodedCredentials);
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        return new HttpEntity<>(body, headers);
    }
    private ResponseEntity<String> executeTokenRequest(String url, HttpEntity<String> entity) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            return restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        } catch (Exception e) {
            System.err.println("Error occurred during token request: " + e.getMessage());
            return null;
        }
    }
    private void parseTokenResponse(String responseBody) {
        JSONObject jsonResponse = new JSONObject(responseBody);
        this.accessToken = jsonResponse.optString("access_token");
        this.refreshToken = jsonResponse.optString("refresh_token", refreshToken);
    }
}
