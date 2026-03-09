package com.adaptivefolk;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;

import java.util.Map;

public class KweebecUtils {
    private static final double CHAT_DISTANCE = 5.0;

    public static void updateKweebecPositions(EntityStore store, Map<Ref<EntityStore>, KweebecData> kweebecs, Vector3d playerPos) {
        for (Map.Entry<Ref<EntityStore>, KweebecData> entry : kweebecs.entrySet()) {
            Ref<EntityStore> ref = entry.getKey();
            KweebecData data = entry.getValue();

            TransformComponent transform = (TransformComponent) store.getStore().getComponent(ref, TransformComponent.getComponentType());
            if (transform != null) {
                data.setPosition(transform.getPosition());
            }

            if (data.getPosition() != null && data.getPosition().distanceTo(playerPos) <= CHAT_DISTANCE) {
                System.out.println("Here!");
            }

            System.out.println("Kweebec Ref: " + ref + " → " + data);
        }
    }
}