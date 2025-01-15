package com.soundsync.main.Controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ParticipantRequestDto {
    @JsonProperty("participant")
    private String participant;

    @JsonProperty("password")
    private String password;

}