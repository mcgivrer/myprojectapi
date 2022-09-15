package com.demoapp.api.services;

import com.demoapp.api.model.Score;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ScoreServiceMapImplTest {

    ScoreServiceMapImpl scoreServiceMap;

    @BeforeEach
    public void setup() {
        scoreServiceMap = new ScoreServiceMapImpl("/test-scores.csv");
    }

    @AfterEach
    public void tearDown() {
        scoreServiceMap = null;
    }

    @Test
    void getScoresWithMax() {
        Collection<Score> scores = scoreServiceMap.getScores(10);
        assertEquals(10, scores.size(), "the scores has not been collected.");
    }

    void getScoresWith2Max() {
        Collection<Score> scores = scoreServiceMap.getScores(2);
        assertEquals(2, scores.size(), "the scores has not been collected.");
    }

    @Test
    void getScoresWithOffsetAndMax() {
        Collection<Score> scores = scoreServiceMap.getScores(5, 10);
        assertEquals(5, scores.size(), "the scores has not been collected.");
    }

    @Test
    void getScoreOnUUID() {
        Score score = scoreServiceMap.getScore("5163eea0-763b-422e-b04f-9f740dda22a7");
        assertAll(
                () -> assertEquals("5163eea0-763b-422e-b04f-9f740dda22a7", score.getUuid().toString()),
                () -> assertEquals("McGivrer1", score.getPlayerName()),
                () -> assertEquals(1000000, score.getValue()),
                () -> assertEquals(
                        ZonedDateTime.parse("2022-09-15T22:42:04+01:00[Europe/Paris]")
                                .toLocalDateTime(), score.getCreatedAt())
        );
    }

    @Test
    void getScoreOnPlayerName() {
        Collection<Score> playerScores = scoreServiceMap.findScoreByPlayerName("McGivrer2");
        assertEquals(1, playerScores.size());
        playerScores = scoreServiceMap.findScoreByPlayerName("McGivrer4");
        assertEquals(2, playerScores.size());
        playerScores = scoreServiceMap.findScoreByPlayerName("McGivrer1");
        assertEquals(3, playerScores.size());

    }

    @Test
    void deleteScore() {
        scoreServiceMap.deleteScore("f9814c1f-f700-46dd-9930-794aee11139b");
        assertEquals(9, scoreServiceMap.scores.size());
    }

    @Test
    void addScore() {
        Score score = new Score(null, "McGivrer2000", 20000000, LocalDateTime.now());
        int scoresSize = scoreServiceMap.scores.size();
        UUID uuid = scoreServiceMap.addScore(score);
        // verify number of scores
        assertEquals(scoresSize + 1, scoreServiceMap.scores.size());
        //verify if score is created
        assertEquals(uuid, scoreServiceMap.getScore(uuid.toString()).getUuid());
    }

    @Test
    void editScore() {
    }

    @Test
    void generateRandomUUIDs() {
        ScoreServiceMapImpl.generateRandomUUIDs();
    }
}