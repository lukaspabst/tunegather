package com.soundsync.main.Controller;

import com.soundsync.main.Controller.dto.ErrorResponse;
import com.soundsync.main.model.Room;
import com.soundsync.main.model.SongSuggestion;
import com.soundsync.main.service.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {
    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping
    public ResponseEntity<?> createRoom(@RequestParam String host) {
        try {
            Room room = roomService.createRoom(host);
            return ResponseEntity.status(201).body(room);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse(500, "Internal Server Error", "Failed to create room."));
        }
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<?> getRoom(@PathVariable String roomId) {
        Room room = roomService.getRoom(roomId);
        if (room == null) {
            return ResponseEntity.status(404).body(new ErrorResponse(404, "Not Found", "Room not found."));
        }
        return ResponseEntity.ok(room);
    }

    @PostMapping("/{roomId}/suggestions")
    public ResponseEntity<?> addSuggestion(@PathVariable String roomId, @RequestBody SongSuggestion suggestion) {
        try {
            roomService.addSuggestion(roomId, suggestion);
            return ResponseEntity.ok("Suggestion added successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(new ErrorResponse(404, "Not Found", e.getMessage()));
        }
    }

    @DeleteMapping("/{roomId}/suggestions")
    public ResponseEntity<?> removeSuggestion(@PathVariable String roomId, @RequestBody SongSuggestion suggestion) {
        try {
            roomService.removeSuggestion(roomId, suggestion);
            return ResponseEntity.ok("Suggestion removed successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(new ErrorResponse(404, "Not Found", e.getMessage()));
        }
    }

    @PostMapping("/{roomId}/participants")
    public ResponseEntity<?> addParticipant(@PathVariable String roomId, @RequestParam String participant) {
        try {
            roomService.addParticipant(roomId, participant);
            return ResponseEntity.ok("Participant added successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(new ErrorResponse(404, "Not Found", e.getMessage()));
        }
    }

    @DeleteMapping("/{roomId}/participants")
    public ResponseEntity<?> removeParticipant(@PathVariable String roomId, @RequestParam String participant) {
        try {
            roomService.removeParticipant(roomId, participant);
            return ResponseEntity.ok("Participant removed successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(new ErrorResponse(404, "Not Found", e.getMessage()));
        }
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<?> deleteRoom(@PathVariable String roomId) {
        try {
            roomService.deleteRoom(roomId);
            return ResponseEntity.ok("Room deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(new ErrorResponse(404, "Not Found", e.getMessage()));
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAllRooms() {
        roomService.deleteAllRooms();
        return ResponseEntity.ok("All rooms deleted successfully.");
    }
}
