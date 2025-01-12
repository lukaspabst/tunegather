package com.soundsync.main.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SongSuggestion {
    private String id;
    private String title;
    private String artist;
    private int duration;
    private String cover;
    private int upVotes;
    private int downVotes;

}
