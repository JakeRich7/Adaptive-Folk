package com.adaptivefolk;

import com.hypixel.hytale.common.plugin.PluginManifest;
import com.hypixel.hytale.component.*;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.NPCPlugin;
import javax.annotation.Nonnull;

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
        this.getEventRegistry().registerGlobal(PlayerChatEvent.class, ListenerChat::Chat);
        ComponentRegistryProxy<EntityStore> registry = this.getEntityStoreRegistry();
        registry.registerSystem(new ListenerSpawn());

        if (KweebecAiResponse.hasModel()) {
            LOGGER.atInfo().log("Ollama model detected. Warming up...");

            KweebecAiResponse.getResponseWarmupAsync()
                    .thenAccept(response -> LOGGER.atInfo().log("Ollama warm-up completed"))
                    .exceptionally(e -> {
                        LOGGER.atWarning().log("Ollama warm-up failed: " + e.getMessage());
                        return null;
                    });

        } else {
            LOGGER.atWarning().log("No Ollama model detected. Using fallback responses only.");
        }
    }
}