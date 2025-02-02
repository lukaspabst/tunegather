package com.soundsync.main.service;

import com.soundsync.main.model.SongSuggestion;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchTracksRequest;
import se.michaelthelin.spotify.requests.data.tracks.GetTrackRequest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpotifyService {

    private final SpotifyAuthService spotifyAuthService;
    private final SpotifyApi spotifyApi;

    public SpotifyService(SpotifyAuthService spotifyAuthService) {
        this.spotifyAuthService = spotifyAuthService;
        this.spotifyApi = new SpotifyApi.Builder()
                .setAccessToken(spotifyAuthService.getAccessToken())
                .build();
    }

    private void refreshSpotifyApi() {
        String accessToken = spotifyAuthService.getAccessToken();
        this.spotifyApi.setAccessToken(accessToken);
    }

    public List<SongSuggestion> searchSongs(String query) {
        try {
            refreshSpotifyApi();

            SearchTracksRequest searchTracksRequest = spotifyApi.searchTracks(query).limit(10).build();
            Track[] tracks = searchTracksRequest.execute().getItems();

            return Arrays.stream(tracks)
                    .map(track -> new SongSuggestion(track.getId(), track.getName(), track.getArtists()[0].getName(), track.getDurationMs(), Arrays.toString(track.getAlbum().getImages()), track.getHref(), track.getUri(), track.getPreviewUrl()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public SongSuggestion getSongById(String songId) {
        try {
            refreshSpotifyApi();
            GetTrackRequest getTrackRequest = spotifyApi.getTrack(songId).build();
            Track track = getTrackRequest.execute();

            return new SongSuggestion(
                    track.getId(),
                    track.getName(),
                    track.getArtists()[0].getName(),
                    track.getDurationMs(),
                    Arrays.toString(track.getAlbum().getImages()),
                    track.getHref(),
                    track.getUri(),
                    track.getPreviewUrl()
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getAccessToken() {
        return spotifyAuthService.getAccessToken();
    }
}