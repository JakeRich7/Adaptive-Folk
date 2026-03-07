package com.adaptivefolk;

import com.hypixel.hytale.common.plugin.PluginManifest;
import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.RefSystem;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.NPCPlugin;
import com.hypixel.hytale.server.npc.entities.NPCEntity;


import javax.annotation.Nonnull;

/**
 * This class serves as the entrypoint for your plugin. Use the setup method to register into game registries or add
 * event listeners.
 */
public class PluginEntry extends JavaPlugin {

    public static final PluginManifest MANIFEST = PluginManifest
            .corePlugin(PluginEntry.class)
            .depends(NPCPlugin.class)
            .build();

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    public PluginEntry(@Nonnull JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        LOGGER.atInfo().log("Setting up plugin " + this.getName());
        this.getEventRegistry().registerGlobal(PlayerChatEvent.class, Chat::EchoPlayer);
        ComponentRegistryProxy<EntityStore> registry = this.getEntityStoreRegistry();
        registry.registerSystem(new SpawnFilter());
    }

    public static class SpawnFilter extends RefSystem<EntityStore> {
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

            if (reason != AddReason.SPAWN) {
                return;
            }

            NPCEntity npc = (NPCEntity) store.getComponent(ref, NPCEntity.getComponentType());
            if (npc != null) {
                String entityType = npc.getRoleName();

                if (entityType.startsWith("Kweebec")) {
                    TransformComponent transform =
                            (TransformComponent) store.getComponent(ref, TransformComponent.getComponentType());

                    if (transform != null) {
                        System.out.println("Spawned " + entityType + " at " + transform.getPosition());
                        System.out.println(npc);
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
}