package com.demoapp.api.model;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public class Score {
    private UUID uuid;
    private String playerName;
    private long value;
    private LocalDateTime createdAt;

    public Score(UUID id, String playerName, long score, LocalDateTime createdAt) {
        this.uuid = id;
        this.playerName = playerName;
        this.value = score;
        this.createdAt = createdAt;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}

