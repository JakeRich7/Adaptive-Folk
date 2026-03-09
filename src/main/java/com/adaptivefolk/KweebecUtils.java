package com.adaptivefolk;

import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.component.Ref;

import java.util.Map;

public class KweebecUtils {
    private static final double CHAT_DISTANCE = 5.0;
    private static final int MAX_RETRIES = 3;

    public static void updateKweebecPositions(EntityStore store, Map<Ref<EntityStore>, KweebecData> kweebecs, Vector3d playerPos) {
        KweebecData closest = null;
        double closestDistance = Double.MAX_VALUE;

        for (Map.Entry<Ref<EntityStore>, KweebecData> entry : kweebecs.entrySet()) {
            Ref<EntityStore> ref = entry.getKey();
            KweebecData data = entry.getValue();

            TransformComponent transform = null;

            // Retry logic for fetching TransformComponent
            for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
                try {
                    transform = (TransformComponent) store.getStore().getComponent(ref, TransformComponent.getComponentType());
                    if (transform != null) break; // Success
                } catch (IllegalStateException e) {
                    System.out.println("Attempt " + attempt + " failed for " + ref + ": " + e.getMessage());
                }
            }

            // Update position if successful
            if (transform != null) {
                data.setPosition(transform.getPosition());
            }

            System.out.println("Kweebec Ref: " + ref + " → " + data);

            // Use last known position or updated position for closest check
            if (data.getPosition() != null) {
                double distance = data.getPosition().distanceTo(playerPos);
                if (distance <= CHAT_DISTANCE && distance < closestDistance) {
                    closestDistance = distance;
                    closest = data;
                    System.out.println("here!");
                }
            }
        }

        if (closest != null) {
            System.out.println("Speaking to closest Kweebec: " + closest + " (Distance: " + closestDistance + ")");
        }
    }
}