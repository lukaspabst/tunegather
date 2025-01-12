package com.soundsync.main.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SongSuggestion {
    @JsonProperty("id")
    private String id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("artist")
    private String artist;

    @JsonProperty("duration")
    private int duration;

    @JsonProperty("cover")
    private String cover;

    @JsonProperty("upVotes")
    private int upVotes;

    @JsonProperty("downVotes")
    private int downVotes;
}