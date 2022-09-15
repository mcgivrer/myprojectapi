package com.demoapp.api.services;

import com.demoapp.api.model.Score;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ScoreServiceMapImpl implements ScoreService {

    Map<String, Score> scores;

    public ScoreServiceMapImpl(String pathToDataFile) {
        scores = new ConcurrentHashMap<>();
        loadScoreTableFromFile(pathToDataFile);
    }

    private void loadScoreTableFromFile(String scoreFilePath) {
        try {
            InputStream is = ScoreServiceMapImpl.class.getResourceAsStream(scoreFilePath);
            String scoreFile = new String(is.readAllBytes());
            if (Optional.ofNullable(scoreFile).isPresent()) {
                Arrays.asList(scoreFile.split("\n")).stream().forEach(s -> {
                    String[] data = s.split(";");
                    importDataLine(data);
                });
            }
        } catch (Exception e) {
            System.out.println("ERR : ScoreServiceMapImpl | Unable to read file from " + scoreFilePath + " : " + e.getMessage());
        }
    }

    private void importDataLine(String[] dataLine) {
        UUID uuid = UUID.fromString(dataLine[0]);
        String playerName = dataLine[1];
        long scoreValue = Long.parseLong(dataLine[2]);
        String dateToParse = dataLine[3].substring(0, dataLine[3].length() - 1);
        ZonedDateTime createdAt = ZonedDateTime.parse(dateToParse);
        scores.put(uuid.toString(), new Score(uuid, playerName, scoreValue, createdAt.toLocalDateTime()));
    }

    @Override
    public Collection<Score> getScores(int lastRecordNumber) {
        return scores.values().stream()
                .sorted((s1, s2) -> s1.getValue() < s2.getValue() ? 1 : -1)
                .limit(lastRecordNumber)
                .collect(Collectors.toList());
    }

    public Collection<Score> getScores(int offset, int lastRecordNumber) {
        if (offset < scores.size() - 1) ;
        return scores.values().stream()
                .sorted((s1, s2) -> s1.getValue() < s2.getValue() ? 1 : -1)
                .skip(offset)
                .limit(lastRecordNumber)
                .collect(Collectors.toList());
    }

    public int getScoreNumber() {
        return scores.size();
    }

    @Override
    public Score getScore(String uuid) {
        return scores.get(uuid);
    }

    @Override
    public void deleteScore(String uuid) {
        scores.remove(uuid);
    }

    @Override
    public UUID addScore(Score score) {
        UUID uuid = UUID.randomUUID();
        score.setUuid(uuid);
        scores.put(score.getUuid().toString(), score);
        writeScoreToFile(score);
        return uuid;
    }

    private void writeScoreToFile(Score score) {
        // TODO implement file writing
    }

    @Override
    public Score editScore(Score scoreToEdit) {
        return null;
    }


    public static void generateRandomUUIDs() {
        DateTimeFormatter dtf = DateTimeFormatter.ISO_DATE_TIME;
        String uuids = "";
        for (int i = 0; i < 10; i++) {
            String uuid = UUID.randomUUID().toString()
                    + ";McGivrer;"
                    + (1000000 - i * 100000) + ";"
                    + DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now());
            System.out.println(uuid);
        }
    }

    @Override
    public Collection<Score> findScoreByPlayerName(String playerNameToSearchScoreFor) {
        return scores.values().stream().filter(s -> s.getPlayerName().equals(playerNameToSearchScoreFor)).collect(Collectors.toList());
    }
}
