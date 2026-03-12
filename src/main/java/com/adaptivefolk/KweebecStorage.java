package com.adaptivefolk;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

public class KweebecStorage {
    private static final Path KWEEBEC_FOLDER = Paths.get("mods", "Adaptive_Folk_Kweebecs");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    static {
        try {
            if (!Files.exists(KWEEBEC_FOLDER)) {
                Files.createDirectories(KWEEBEC_FOLDER);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static KweebecProfile loadOrCreateInitialDoc(UUID uuid) {
        Path file = KWEEBEC_FOLDER.resolve(uuid.toString() + ".json");

        if (Files.exists(file)) {
            try {
                String content = Files.readString(file);
                return GSON.fromJson(content, KweebecProfile.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String randomName = KweebecNameGenerator.getRandomName();
        KweebecProfile profile = new KweebecProfile(uuid, randomName);
        saveInitialDoc(uuid, profile);
        return profile;
    }

    public static void saveInitialDoc(UUID uuid, KweebecProfile profile) {
        Path file = KWEEBEC_FOLDER.resolve(uuid.toString() + ".json");
        try {
            String json = GSON.toJson(profile);
            Files.writeString(file, json, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void removeDoc (UUID uuid) {
        Path file = KWEEBEC_FOLDER.resolve(uuid.toString() + ".json");
        try {
            Files.deleteIfExists(file);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class KweebecProfile {
        private UUID uuid;
        private String name;

        public KweebecProfile(UUID uuid, String name) {
            this.uuid = uuid;
            this.name = name;
        }

        public UUID getUuid() { return uuid; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }
}