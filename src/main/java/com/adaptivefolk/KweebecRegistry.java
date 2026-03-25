package com.adaptivefolk;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class KweebecRegistry {
    private static final Map<Ref<EntityStore>, KweebecData> kweebecs = new ConcurrentHashMap<>();

    public static void add(KweebecData kweebec) {
        kweebecs.put(kweebec.getReference(), kweebec);
    }

    public static void remove(Ref<EntityStore> reference) {
        kweebecs.remove(reference);
    }

    public static KweebecData get(Ref<EntityStore> reference) {
        return kweebecs.get(reference);
    }

    public static Map<Ref<EntityStore>, KweebecData> getAll() {
        return kweebecs;
    }
}