package com.adaptivefolk;  // same package as your plugin

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.RefSystem;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.entity.UUIDComponent;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.entities.NPCEntity;

import javax.annotation.Nonnull;

public class SpawnFilter extends RefSystem<EntityStore> {
    @Nonnull
    @Override
    public Query<EntityStore> getQuery() {
        return Query.any();
    }

    @Override
    public void onEntityAdded(@Nonnull Ref<EntityStore> ref,
                              @Nonnull AddReason reason,
                              @Nonnull Store<EntityStore> store,
                              @Nonnull CommandBuffer<EntityStore> commandBuffer) {

        NPCEntity npc = (NPCEntity) store.getComponent(ref, NPCEntity.getComponentType());
        if (npc != null) {
            String entityType = npc.getRoleName();

            if (entityType != null && entityType.startsWith("Kweebec")) {
                TransformComponent transform =
                        (TransformComponent) store.getComponent(ref, TransformComponent.getComponentType());

                UUIDComponent uuidComponent =
                        (UUIDComponent) store.getComponent(ref, UUIDComponent.getComponentType());

                if (transform != null) {
                    Vector3d pos = transform.getPosition();
                    Ref<EntityStore> reference = npc.getReference();

//                    System.out.println("Spawned/Loaded " + entityType + " at " + pos);

                    KweebecRegistry.add(new KweebecData(reference, pos));

                    System.out.println("NPC UUID: " + uuidComponent.getUuid());
                    System.out.println(npc);

//                    KweebecRegistry.getAll().forEach((refer, data) -> {
//                        System.out.println("Ref: " + refer + " → " + data);
//                    });
                }
            }
        }
    }

    @Override
    public void onEntityRemove(@Nonnull Ref<EntityStore> ref,
                               @Nonnull RemoveReason reason,
                               @Nonnull Store<EntityStore> store,
                               @Nonnull CommandBuffer<EntityStore> commandBuffer) {
    }
}