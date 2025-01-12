package com.soundsync.main;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soundsync.main.model.Room;
import com.soundsync.main.repository.RoomRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.client.RestClientSsl;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class DataInitializer {

    private final RoomRepository roomRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public DataInitializer(RoomRepository roomRepository, ObjectMapper objectMapper) {
        this.roomRepository = roomRepository;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void loadData() {
        try {
            ClassPathResource resource = new ClassPathResource("sample_data.json");
            File file = resource.getFile();
            List<Room> rooms = objectMapper.readValue(file, new TypeReference<List<Room>>() {});
            roomRepository.saveAll(rooms);
            System.out.println("Sample data loaded into MongoDB.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void dumpData() {
            roomRepository.deleteAll();
            System.out.println("Database cleared.");
    }
}