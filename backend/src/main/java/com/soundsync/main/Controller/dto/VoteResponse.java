package com.soundsync.main.Controller.dto;

public class VoteResponse {
    private String songId;
    private String voteType;
    private int voteCount;

    public VoteResponse() {
    }
    public VoteResponse(String songId, String voteType, int voteCount) {
        this.songId = songId;
        this.voteType = voteType;
        this.voteCount = voteCount;
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

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }
}
