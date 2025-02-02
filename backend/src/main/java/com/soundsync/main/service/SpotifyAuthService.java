package com.soundsync.main.service;

import com.soundsync.main.Config.SpotifyConfig;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
@Service
public class SpotifyAuthService {

    private final SpotifyConfig spotifyConfig;
    private String accessToken;
    private long tokenExpirationTime;

    public SpotifyAuthService(SpotifyConfig spotifyConfig) {
        this.spotifyConfig = spotifyConfig;
    }

    public String getAccessToken() {
        if (System.currentTimeMillis() > tokenExpirationTime) {
            retrieveNewAccessToken();
        }
        return accessToken;
    }
    private void retrieveNewAccessToken() {

        String credentials = spotifyConfig.getClientId() + ":" + spotifyConfig.getClientSecret();
        String base64EncodedCredentials = new String(Base64.getEncoder().encode(credentials.getBytes()));

        String url = "https://accounts.spotify.com/api/token";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64EncodedCredentials);
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        String body = "grant_type=client_credentials";
        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);


        String responseBody = response.getBody();
        assert responseBody != null;

        this.accessToken = responseBody.substring(responseBody.indexOf("access_token\":\"") + 15, responseBody.indexOf("\",\"token_type"));
        this.tokenExpirationTime = System.currentTimeMillis() + 3600000;
    }
}
