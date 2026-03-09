package com.adaptivefolk;

import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.component.Ref;

import java.util.Map;

public class KweebecUtils {
    private static final double CHAT_DISTANCE = 5.0;
    private static final float FACING_THRESHOLD = 0.7f; // cosine of ~45° cone
    private static final int MAX_RETRIES = 3;

    public static void updateKweebecPositions(EntityStore store, Map<Ref<EntityStore>, KweebecData> kweebecs, Vector3d playerPos, Vector3f playerHeadRotation) {
        KweebecData closest = null;
        double closestDistance = Double.MAX_VALUE;

        for (Map.Entry<Ref<EntityStore>, KweebecData> entry : kweebecs.entrySet()) {
            Ref<EntityStore> ref = entry.getKey();
            KweebecData data = entry.getValue();

            TransformComponent transform = null;

            // Retry logic for TransformComponent
            for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
                try {
                    transform = (TransformComponent) store.getStore().getComponent(ref, TransformComponent.getComponentType());
                    if (transform != null) break;
                } catch (IllegalStateException e) {
                    System.out.println("Attempt " + attempt + " failed for " + ref + ": " + e.getMessage());
                }
            }

            // Update position if successful
            if (transform != null) {
                data.setPosition(transform.getPosition());
            }

            System.out.println("Kweebec Ref: " + ref + " → " + data);

            // Proximity + facing check
            if (data.getPosition() != null) {
                Vector3d toKweebec = data.getPosition().subtract(playerPos).normalize();
                Vector3f headDir = playerHeadRotation.normalize();

                double distance = data.getPosition().distanceTo(playerPos);
                double dot = headDir.dot(toKweebec.toVector3f());

                if (distance <= CHAT_DISTANCE && dot >= FACING_THRESHOLD && distance < closestDistance) {
                    closestDistance = distance;
                    closest = data;
                }
            }
        }

        if (closest != null) {
            System.out.println("Speaking to closest Kweebec: " + closest + " (Distance: " + closestDistance + ")");
        }
    }
}