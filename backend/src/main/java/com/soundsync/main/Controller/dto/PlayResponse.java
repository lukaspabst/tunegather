package com.soundsync.main.Controller.dto;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PlayResponse {
    private String songUri;
    private String songName;
    private String songArtist;

    public PlayResponse(String songUri, String songName, String songArtist) {
        this.songUri = songUri;
        this.songName = songName;
        this.songArtist = songArtist;
    }

}
