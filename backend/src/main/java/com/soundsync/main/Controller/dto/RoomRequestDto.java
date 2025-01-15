package com.soundsync.main.Controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RoomRequestDto {
    @JsonProperty("title")
    private String title;

    @JsonProperty("password")
    private String password;

    @JsonProperty("host")
    private String host;

    @JsonProperty("playlistThreshold")
    private int playlistThreshold;

}