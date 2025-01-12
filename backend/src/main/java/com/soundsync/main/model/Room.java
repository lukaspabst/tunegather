package com.soundsync.main.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

@Document(collection = "rooms")
public class Room {

    @Id
    @JsonProperty("id")
    private String id;

    @JsonProperty("host")
    private String host;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public List<SongSuggestion> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<SongSuggestion> suggestions) {
        this.suggestions = suggestions;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
