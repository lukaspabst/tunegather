package com.soundsync.main.Controller.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class VoteRequest {
    private String roomId;
    private String songId;
    private String voteType;
}
