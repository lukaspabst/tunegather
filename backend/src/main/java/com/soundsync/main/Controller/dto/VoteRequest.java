package com.soundsync.main.Controller.dto;

public class VoteRequest {
    private String roomId;
    private String songId;
    private String voteType;

    public VoteRequest() {
    }

    public VoteRequest(String roomId, String songId, String voteType) {
        this.roomId = roomId;
        this.songId = songId;
        this.voteType = voteType;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }

    public String getVoteType() {
        return voteType;
    }

    public void setVoteType(String voteType) {
        this.voteType = voteType;
    }
}
