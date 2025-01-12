package com.soundsync.main.Controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class VoteResponse {
    private String songId;
    private String voteType;
    private int voteCount;
}
