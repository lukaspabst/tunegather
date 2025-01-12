package com.soundsync.main.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "rooms")
@Getter
@Setter
@RequiredArgsConstructor
public class Room {
    @Id
    private String id;
    private String host;
    private List<String> participants;
    private List<SongSuggestion> suggestions;
    private String qrCode;
    private String link;
}
