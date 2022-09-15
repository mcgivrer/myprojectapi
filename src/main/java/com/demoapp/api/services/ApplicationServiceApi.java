package com.demoapp.api.services;

import com.demoapp.api.model.Score;
import com.demoapp.api.services.http.StandardResponse;
import com.demoapp.api.services.http.StatusResponse;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.UUID;

import static spark.Spark.*;

public class ApplicationServiceApi {
    public static void main(String[] args) {
        final ScoreService scoreService = new ScoreServiceMapImpl("/data/scores.csv");

        post("/scores", (request, response) -> {
            response.type("application/json");

            Score score = createGson().fromJson(request.body(), Score.class);
            scoreService.addScore(score);

            return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS));
        });

        get("/scores", (request, response) -> {
            response.type("application/json");

            Collection<Score> scores = scoreService.getScores(10);
            Gson gson = createGson();
            JsonElement responseScore = gson.toJsonTree(scores);
            StandardResponse standardResp = new StandardResponse(StatusResponse.SUCCESS, responseScore);
            return gson.toJson(standardResp);
        });
        get("/scores/for/:playerName", (request, response) -> {
            response.type("application/json");

            Collection<Score> scores = scoreService.findScoreByPlayerName(request.params(":playerName"));
            Gson gson = createGson();
            JsonElement responseScore = gson.toJsonTree(scores);
            StandardResponse standardResp = new StandardResponse(StatusResponse.SUCCESS, responseScore);
            return gson.toJson(standardResp);
        });
        get("/scores/:id", (request, response) -> {
            response.type("application/json");

            return createGson().toJson(new StandardResponse(StatusResponse.SUCCESS, createGson().toJsonTree(scoreService.getScore(request.params(":id")))));
        });

        put("/scores/:id", (request, response) -> {
            response.type("application/json");

            Score toEdit = createGson().fromJson(request.body(), Score.class);
            Score editedScore = scoreService.editScore(toEdit);

            if (editedScore != null) {
                return createGson().toJson(new StandardResponse(StatusResponse.SUCCESS, createGson().toJsonTree(editedScore)));
            } else {
                return createGson().toJson(new StandardResponse(StatusResponse.ERROR, createGson().toJson("Score not found or error in edit")));
            }
        });

        delete("/scores/:id", (request, response) -> {
            response.type("application/json");

            scoreService.deleteScore(request.params(":id"));
            return createGson().toJson(new StandardResponse(StatusResponse.SUCCESS, "score deleted"));
        });


    }

    public static Gson createGson() {
        return new GsonBuilder().registerTypeAdapter(UUID.class, new JsonSerializer<UUID>() {
                    @Override
                    public JsonElement serialize(UUID src, Type typeOfSrc, JsonSerializationContext context) {
                        return new JsonPrimitive(src.toString());
                    }
                })
                .registerTypeAdapter(UUID.class, new JsonDeserializer<UUID>() {
                    @Override
                    public UUID deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        return UUID.fromString(json.getAsString());
                    }
                })
                .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, type, jsonDeserializationContext) ->
                        ZonedDateTime.parse(json.getAsJsonPrimitive().getAsString()).toLocalDateTime())
                .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (json, type, jsonSerializationContext) -> new JsonPrimitive(json.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .create();
    }
}

