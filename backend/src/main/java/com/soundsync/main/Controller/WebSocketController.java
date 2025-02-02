package com.soundsync.main.Controller;

import com.soundsync.main.Controller.dto.VoteRequest;
import com.soundsync.main.Controller.dto.VoteResponse;
import com.soundsync.main.model.SongSuggestion;
import com.soundsync.main.service.SpotifyService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class WebSocketController {

    private final SpotifyService spotifyService;

    public WebSocketController(SpotifyService spotifyService) {
        this.spotifyService = spotifyService;
    }

    @MessageMapping("/vote")
    @SendTo("/topic/votes")
    public VoteResponse handleVote(VoteRequest voteRequest) {
        return new VoteResponse(voteRequest.getSongId(), voteRequest.getVoteType(), 42);
    }

    @MessageMapping("/search")
    @SendTo("/topic/searchResults")
    public List<SongSuggestion> search(String query) {
        return spotifyService.searchSongs(query);
    }

    @GetMapping("/token")
    public String getAccessToken() {
        return spotifyService.getAccessToken();
    }
}