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
    private static final int MAX_FACING = 90;

    public static void updateKweebecPositions(EntityStore store, Map<Ref<EntityStore>, KweebecData> kweebecs, Vector3d playerPos, Vector3f playerHeadRotation) {
        KweebecData closest = null;
        double closestDistance = Double.MAX_VALUE;

        for (Map.Entry<Ref<EntityStore>, KweebecData> entry : kweebecs.entrySet()) {
            Ref<EntityStore> ref = entry.getKey();
            KweebecData data = entry.getValue();

            TransformComponent transform = (TransformComponent) store.getStore().getComponent(ref, TransformComponent.getComponentType());
            if (transform != null) {
                data.setPosition(transform.getPosition());
            }

            if (data.getPosition() != null) {
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
                }
            }
        }

        if (closest != null) {
            System.out.println("Speaking to closest Kweebec: " + closest + " (Distance: " + closestDistance + ")");
            System.out.println(closest);
        }
    }

    private static double normalizeRadians(double angle) {
        angle = angle % (2 * PI);
        if (angle > PI) angle -= 2 * PI;
        if (angle < -PI) angle += 2 * PI;
        return angle;
    }
}