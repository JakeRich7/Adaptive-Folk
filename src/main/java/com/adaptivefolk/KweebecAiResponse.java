package com.adaptivefolk;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class KweebecAiResponse {

    private static final String OLLAMA_URL = "http://127.0.0.1:11434/api/generate";
    private static final String TAGS_URL = "http://127.0.0.1:11434/api/tags";
    private static final HttpClient client = HttpClient.newHttpClient();

    private static String selectedModel = null;
    private static boolean modelChecked = false;

    // This list 'prefers' models from top to bottom if multiple models are found, though ANY valid Ollama model can be used (just download that model only)
    private static final List<String> PREFERRED_MODELS = List.of(
            "llama3.2:3b",         // Very capable, good speed
            "llama3.2:1b",         // Lightweight version: faster, still decent quality
            "gemma3:4b",           // Balanced medium model: good quality, moderate speed
            "gemma3:1b",           // Fastest and lightest, suitable for low-end setups
            "qwen2.5:3b",          // Alternative balanced chat model
            "qwen2.5:7b",          // Larger Qwen variant, higher-quality dialogue
            "mistral:7b",          // Stronger midweight model, richer responses
            "gemma3:12b"           // Largest model (in this list): highest quality, heavy on resources
    );

    public static CompletableFuture<String> generateMemorySummary(UUID npcUUID, String playerText) {
        List<String> recentMemory = KweebecStorage.getRecentMessages(npcUUID, 250);
        String memoryText = String.join("\n", recentMemory);

        String summaryPrompt = String.format(
                "Please search through these logs:\n%s\n\n" +
                "Can you return a one sentence detailed summary that contains any data from these logs that is directly relevant in responding to this prompt: \n'%s'\n\n" +
                "You MUST only return information from these logs. Do NOT make anything up. Return nothing rather than returning something that is not in these logs." +
                "Your return should not have any extra quotation marks or newlines",
                playerText,
                memoryText
        );

        JsonObject json = new JsonObject();
        json.addProperty("model", selectedModel);
        json.addProperty("prompt", summaryPrompt);
        json.addProperty("stream", false);

        JsonObject options = new JsonObject();
        options.addProperty("num_predict", 50);
        options.addProperty("temperature", 0.5);
        json.add("options", options);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(OLLAMA_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(body -> {
                    JsonObject obj = JsonParser.parseString(body).getAsJsonObject();
                    return obj.get("response").getAsString().trim();
                });
    }

    public static CompletableFuture<String> getResponseAsync(String playerText, String npcName, String npcRoleName, UUID npcUUID) {
        return generateMemorySummary(npcUUID, playerText)
                .thenCompose(summary -> {
                    String prompt = String.format(
                            "You are %s, a Kweebec %s from Hytale. You have short fur. Your skin color is an earthy tone. You never move more than a few blocks.\n\n" +
                            "Summary of past interactions (for context). This is background knowledge. Only use it if directly relevant.:\n%s\n\n" +
                            "Speak warmly and simply.\n" +
                            "Try not to abbreviate words.\n" +
                            "Stay in character at all times.\n" +
                            "Always respond in English UNLESS you are directly spoken to in another language\n" +
                            "Keep responses under 30 words and in one sentence.\n" +
                            "Make sure your response is one is in plain text and has no extra quotation marks.\n\n" +
                            "Player says: \"%s\"\n" +
                            "%s: ",
                            npcName,
                            npcRoleName,
                            summary,
                            playerText,
                            npcName
                    );

                    System.out.println("Role: " + npcRoleName);
                    System.out.println("Summary: " + summary);

                    JsonObject json = new JsonObject();
                    json.addProperty("model", selectedModel);
                    json.addProperty("prompt", prompt);
                    json.addProperty("stream", false);

                    JsonObject options = new JsonObject();
                    options.addProperty("num_predict", 60);
                    options.addProperty("temperature", 0.7);
                    options.addProperty("repeat_penalty", 1.1);
                    options.addProperty("top_p", 0.9);
                    json.add("options", options);

                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(OLLAMA_URL))
                            .header("Content-Type", "application/json")
                            .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                            .build();

                    return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                            .thenApply(HttpResponse::body)
                            .thenApply(body -> {
                                JsonObject obj = JsonParser.parseString(body).getAsJsonObject();
                                return obj.get("response").getAsString().trim();
                            });
                });
    }

    private static void initializeModel() {
        modelChecked = true;

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(TAGS_URL))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();

            JsonArray models = json.getAsJsonArray("models");

            List<String> available = new ArrayList<>();
            for (JsonElement el : models) {
                JsonObject obj = el.getAsJsonObject();
                available.add(obj.get("name").getAsString());
            }

            // Preferred match
            for (String preferred : PREFERRED_MODELS) {
                if (available.contains(preferred)) {
                    selectedModel = preferred;
                    break;
                }
            }

            // Fallback to any
            if (selectedModel == null && !available.isEmpty()) {
                selectedModel = available.getFirst();
            }

            if (selectedModel != null) {
                System.out.println("[AI] Using model: " + selectedModel);
            }
        } catch (Exception e) {
            selectedModel = null;
            System.out.println("[AI] Failed to detect models: " + e.getMessage());
        }
    }

    public static boolean hasModel() {
        if (!modelChecked) initializeModel();
        return selectedModel != null;
    }

    public static CompletableFuture<String> getResponseWarmupAsync(String playerText, String npcName) {
        // Simple prompt
        String prompt = String.format(
                "This is a message to get you warmed up."
        );

        // Build JSON body
        JsonObject json = new JsonObject();
        json.addProperty("model", selectedModel);
        json.addProperty("prompt", prompt);
        json.addProperty("stream", false);

        JsonObject options = new JsonObject();
        options.addProperty("num_predict", 60);   // short response for speed
        options.addProperty("temperature", 0.7);  // slight creativity
        options.addProperty("repeat_penalty", 1.1);  // avoids weird looping
        options.addProperty("top_p", 0.9);  // only uses the top 90% of words
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
                    return obj.get("response").getAsString();
                });
    }
}