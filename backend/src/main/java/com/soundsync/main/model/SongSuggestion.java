package com.soundsync.main.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    public SongSuggestion(String id, String name, String artist, int duration, String cover) {
        this.id = id;
        this.title = name;
        this.artist = artist;
        this.duration = duration;
        this.cover = cover;
        this.upVotes = 0;
        this.downVotes = 0;
    }
}