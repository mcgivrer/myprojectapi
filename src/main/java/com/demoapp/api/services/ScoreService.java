package com.demoapp.api.services;

import com.demoapp.api.model.Score;

import java.util.Collection;
import java.util.UUID;

public interface ScoreService {
    Collection<Score> getScores(int lastRecordNumber);

    Collection<Score> getScores(int offset, int lastRecordNumber);

    Collection<Score> findScoreByPlayerName(String playerName);

    int getScoreNumber();

    Score getScore(String uuid);

    void deleteScore(String uuid);

    UUID addScore(Score score);

    Score editScore(Score scoreToEdit);
}
