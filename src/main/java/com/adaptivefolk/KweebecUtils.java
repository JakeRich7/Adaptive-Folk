package com.adaptivefolk;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;

import java.util.Map;

public class KweebecUtils {
    public static void updateKweebecPositions(EntityStore store, Map<Ref<EntityStore>, KweebecData> kweebecs) {
        for (Map.Entry<Ref<EntityStore>, KweebecData> entry : kweebecs.entrySet()) {
            Ref<EntityStore> ref = entry.getKey();
            KweebecData data = entry.getValue();

            TransformComponent transform = (TransformComponent) store.getStore().getComponent(ref, TransformComponent.getComponentType());
            if (transform != null) {
                data.setPosition(transform.getPosition());
            }

            System.out.println("Kweebec Ref: " + ref + " → " + data);
        }
    }
}