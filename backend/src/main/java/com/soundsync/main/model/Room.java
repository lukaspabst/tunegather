package com.soundsync.main.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

@Document(collection = "rooms")
@Getter
@Setter
public class Room {

    @Id
    @JsonProperty("id")
    private String id;

    @JsonProperty("host")
    private String host;

    @JsonProperty("roomTitle")
    private String roomTitle;

    @JsonProperty("entryPercentage")
    private String entryPercentage;

    @JsonProperty("roomPassword")
    private String roomPassword;

    @JsonProperty("participants")
    private List<String> participants;

    @JsonProperty("suggestions")
    private List<SongSuggestion> suggestions;

    @JsonProperty("qrCode")
    private String qrCode;

    @JsonProperty("link")
    private String link;

    public Room() {
    }

    public Room(String id, String host, List<String> participants, List<SongSuggestion> suggestions, String qrCode, String link) {
        this.id = id;
        this.host = host;
        this.participants = participants;
        this.suggestions = suggestions;
        this.qrCode = qrCode;
        this.link = link;
    }
}