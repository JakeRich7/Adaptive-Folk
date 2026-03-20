package com.adaptivefolk;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.entity.UUIDComponent;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.entities.NPCEntity;

import java.util.UUID;

public class EventSpawnRemoval {
    public static void entitySpawn(Ref<EntityStore> ref, Store<EntityStore> store) {

        NPCEntity npc = (NPCEntity) store.getComponent(ref, NPCEntity.getComponentType());

        // Entity filter
        if (npc != null) {
            String entityType = npc.getRoleName();
            if (entityType != null && entityType.startsWith("Kweebec")) {

                TransformComponent transform =
                        (TransformComponent) store.getComponent(ref, TransformComponent.getComponentType());
                if (transform != null) {
                    // Entity Creation (Document Creation or Retrieval + Add Entity to Registry)
                    Vector3d pos = transform.getPosition();
                    Ref<EntityStore> reference = npc.getReference();
                    String role = npc.getRoleName();
                    UUIDComponent uuidComponent =
                            (UUIDComponent) store.getComponent(ref, UUIDComponent.getComponentType());
                    UUID uuid = uuidComponent.getUuid();

                    KweebecStorage.KweebecProfile profile =
                            KweebecStorage.loadOrCreateInitialDoc(uuid);
                    if (profile.getName() == null || profile.getName().isEmpty()) {
                        String randomName = KweebecNameGenerator.getRandomName();
                        profile.setName(randomName);
                        KweebecStorage.saveInitialDoc(uuid, profile);
                    }
                    String name = profile.getName();

                    KweebecRegistry.add(
                            new KweebecData(reference, pos, name, role, uuid)
                    );
                }
            }
        }
    }

    public static void entityRemoval(Ref<EntityStore> ref, Store<EntityStore> store) {
        KweebecData data = KweebecRegistry.get(ref);
        if (data != null) {
            KweebecRegistry.remove(ref);
            UUIDComponent uuidComponent =
                    (UUIDComponent) store.getComponent(ref, UUIDComponent.getComponentType());
            UUID uuid = uuidComponent.getUuid();
            KweebecStorage.removeDoc(uuid);
        }
    }
}
