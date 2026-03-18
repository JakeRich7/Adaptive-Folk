package com.adaptivefolk;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class KweebecAiResponse {

    private static final String OLLAMA_URL = "http://127.0.0.1:11434/api/generate";
    private static final String MODEL_NAME = "phi3:mini"; // fast local AI model

    // Make client static so it can be used in the static method
    private static final HttpClient client = HttpClient.newHttpClient();

    /**
     * Sends player text to Ollama and returns the AI response.
     *
     * @param playerText The text the player says
     * @param npcName    The NPC's name
     * @return The AI response
     */
    public static CompletableFuture<String> getResponseAsync(String playerText, String npcName) {
        // Simple prompt
        String prompt = String.format(
                "You are a Kweebec named %s from Hytale. Respond in-character to the player *only*. " +
                        "Do not repeat the player's words. Do not add any extra instructions, metadata, or commentary. " +
                        "Return a single line response.\nPlayer: \"%s\"",
                npcName, playerText
        );

        // Build JSON body
        JsonObject json = new JsonObject();
        json.addProperty("model", MODEL_NAME);
        json.addProperty("prompt", prompt);
        json.addProperty("stream", false);

        JsonObject options = new JsonObject();
        options.addProperty("num_predict", 60);   // short response for speed
        options.addProperty("temperature", 0.7);  // slight creativity
        json.add("options", options);

        // Build HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(OLLAMA_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                .build();

        // Parse response JSON
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(body -> {
                    JsonObject obj = JsonParser.parseString(body).getAsJsonObject();
                    System.out.println(obj);
                    return obj.get("response").getAsString();
                });
    }
}