package com.soundsync.main.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.soundsync.main.model.Room;
import com.soundsync.main.model.SongSuggestion;
import com.soundsync.main.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
public class RoomService {
    private final RoomRepository roomRepository;

    @Value("${app.base-url}")
    private String baseUrl;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Room createRoom(String host) throws Exception {
        String roomId = UUID.randomUUID().toString();
        String link = baseUrl + roomId;
        String qrCodePath = roomId + "_qr.png";

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(link, BarcodeFormat.QR_CODE, 300, 300);
        BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(qrImage, "PNG", outputStream);
        byte[] qrCodeBytes = outputStream.toByteArray();
        String qrCodeBase64 = Base64.getEncoder().encodeToString(qrCodeBytes);

        System.out.println("QR Code saved to: " + qrCodePath);

        Room room = new Room();
        room.setId(roomId);
        room.setHost(host);
        room.setLink(link);
        room.setQrCode(qrCodeBase64);

        List<String> participants = new ArrayList<>();
        participants.add(host);
        room.setParticipants(participants);
        List<SongSuggestion> suggestions = new ArrayList<>();
        room.setSuggestions(suggestions);

        return roomRepository.save(room);
    }

    public Room getRoom(String roomId) {
        return roomRepository.findById(roomId).orElse(null);
    }

    public void addSuggestion(String roomId, SongSuggestion suggestion) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room not found"));
        room.getSuggestions().add(suggestion);
        roomRepository.save(room);
    }

    public void removeSuggestion(String roomId, SongSuggestion suggestion) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room not found"));
        room.getSuggestions().removeIf(existingSuggestion -> existingSuggestion.equals(suggestion));
        roomRepository.save(room);
    }

    public void addParticipant(String roomId, String participant) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room not found"));
        room.getParticipants().add(participant);
        roomRepository.save(room);
    }

    public void removeParticipant(String roomId, String participant) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room not found"));
        room.getParticipants().removeIf(existingParticipant -> existingParticipant.equals(participant));
        roomRepository.save(room);
    }

    public void deleteRoom(String roomId) {
        if (roomRepository.existsById(roomId)) {
            roomRepository.deleteById(roomId);
        } else {
            throw new RuntimeException("Room not found");
        }
    }

    public void deleteAllRooms() {
        roomRepository.deleteAll();
    }
}
