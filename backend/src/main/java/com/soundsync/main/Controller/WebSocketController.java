package com.soundsync.main.Controller;

import com.soundsync.main.Controller.dto.VoteRequest;
import com.soundsync.main.Controller.dto.VoteResponse;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @MessageMapping("/vote")
    @SendTo("/topic/votes")
    public VoteResponse handleVote(VoteRequest voteRequest) {
        return new VoteResponse(voteRequest.getSongId(), voteRequest.getVoteType(), 42);
    }
}
