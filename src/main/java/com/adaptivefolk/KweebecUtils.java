package com.adaptivefolk;

import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.component.Ref;

import java.util.Map;

import static java.lang.Math.PI;

public class KweebecUtils {
    private static final double CHAT_DISTANCE = 5.0;
    private static final int MAX_RETRIES = 3;
    private static final int MAX_FACING = 90;

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
                // Vector from player to Kweebec in XZ
                double dx = data.getPosition().x - playerPos.x;
                double dz = data.getPosition().z - playerPos.z;
                double distanceXZ = Math.sqrt(dx*dx + dz*dz);

                if (distanceXZ <= CHAT_DISTANCE) {
                    double playerYaw = playerHeadRotation.y;
                    double angleToKweebec = Math.atan2(dx, dz);

                    double delta = normalizeRadians(angleToKweebec - playerYaw);
                    delta = ((delta + PI) % (2 * PI)) - PI;
                    double absDelta = Math.abs(delta);
                    double maxFacing = PI - (Math.toRadians(MAX_FACING) / 2);

                    if (distanceXZ < closestDistance && absDelta >= maxFacing) {
                        closestDistance = distanceXZ;
                        closest = data;
                    }

                    // Debug
//                    System.out.println("Kweebec at: " + data.getPosition());
//                    System.out.println("PlayerPos: " + playerPos);
//                    System.out.println("DistanceXZ: " + distanceXZ);
                    System.out.println("AngleToKweebec: " + angleToKweebec);
                    System.out.println("absDelta: " + absDelta);
                    System.out.println("maxFacing: " + maxFacing);
                }
            }
        }

        if (closest != null) {
            System.out.println("Speaking to closest Kweebec: " + closest + " (Distance: " + closestDistance + ")");
        }
    }

    private static double normalizeRadians(double angle) {
        angle = angle % (2 * PI);
        if (angle > PI) angle -= 2 * PI;
        if (angle < -PI) angle += 2 * PI;
        return angle;
    }
}