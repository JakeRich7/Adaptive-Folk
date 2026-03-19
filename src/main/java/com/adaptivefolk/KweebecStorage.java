package com.adaptivefolk;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.*;
import java.util.*;

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

    // ================== Profile Methods ==================
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

    public static void removeDoc(UUID uuid) {
        String uuidString = uuid.toString();
        Path profileFile = KWEEBEC_FOLDER.resolve(uuidString + ".json");
        Path memoryFile = KWEEBEC_FOLDER.resolve(uuidString + ".jsonl");
        try {
            Files.deleteIfExists(profileFile);
            Files.deleteIfExists(memoryFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void appendMessage(UUID uuid, String role, String message) {
        Path file = KWEEBEC_FOLDER.resolve(uuid.toString() + ".jsonl");
        String sanitized = message.replace("\"", "\\\""); // simple escape
        String line = String.format("{\"name\":\"%s\",\"message\":\"%s\"}%n", role, sanitized);

        try {
            Files.writeString(file, line, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getRecentMessages(UUID uuid, int limit) {
        Path file = KWEEBEC_FOLDER.resolve(uuid.toString() + ".jsonl");
        if (!Files.exists(file)) return Collections.emptyList();

        Deque<String> deque = new ArrayDeque<>(limit);

        try (RandomAccessFile raf = new RandomAccessFile(file.toFile(), "r")) {
            long fileLength = raf.length();
            long pointer = fileLength - 1;
            StringBuilder sb = new StringBuilder();

            while (pointer >= 0) {
                raf.seek(pointer);
                int readByte = raf.readByte();

                if (readByte == '\n') {
                    if (sb.length() > 0) {
                        deque.addFirst(sb.reverse().toString());
                        sb.setLength(0);
                        if (deque.size() > limit) {
                            deque.removeLast();
                        }
                    }
                } else {
                    sb.append((char) readByte);
                }
                pointer--;
            }

            // Add the first line (if file didn't end with \n)
            if (sb.length() > 0) {
                deque.addFirst(sb.reverse().toString());
                if (deque.size() > limit) deque.removeLast();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<>(deque);
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