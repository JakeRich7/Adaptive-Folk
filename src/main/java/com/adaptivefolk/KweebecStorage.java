package com.adaptivefolk;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

public class KweebecStorage {
    private static final Path KWEBEC_FOLDER = Paths.get("mods", "Kweebecs");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    static {
        try {
            if (!Files.exists(KWEBEC_FOLDER)) {
                Files.createDirectories(KWEBEC_FOLDER);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static KweebecProfile loadOrCreate(UUID uuid) {
        Path file = KWEBEC_FOLDER.resolve(uuid.toString() + ".json");

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
        save(profile);
        return profile;
    }

    public static void save(KweebecProfile profile) {
        Path file = KWEBEC_FOLDER.resolve(profile.getUuid().toString() + ".json");
        try {
            String json = GSON.toJson(profile);
            Files.writeString(file, json, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
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