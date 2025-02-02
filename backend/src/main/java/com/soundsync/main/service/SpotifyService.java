package com.soundsync.main.service;

import com.soundsync.main.model.SongSuggestion;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchTracksRequest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpotifyService {

    private final SpotifyApi spotifyApi;

    public SpotifyService(SpotifyAuthService spotifyAuthService) {
        String accessToken = spotifyAuthService.getAccessToken();
        this.spotifyApi = new SpotifyApi.Builder()
                .setAccessToken(accessToken)
                .build();
    }

    public List<SongSuggestion> searchSongs(String query) {
        try {
            SearchTracksRequest searchTracksRequest = spotifyApi.searchTracks(query).build();
            Track[] tracks = searchTracksRequest.execute().getItems();

            return Arrays.stream(tracks)
                    .map(track -> new SongSuggestion(track.getId(), track.getName(), track.getArtists()[0].getName(), track.getDurationMs(), Arrays.toString(track.getAlbum().getImages())))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}