package com.adaptivefolk;

import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;

import java.util.UUID;

public class ListenerChat {
    public static void Chat(PlayerChatEvent event) {
        String message = event.getContent();
        PlayerRef player = event.getSender();
        Vector3d playerPos = player.getTransform().getPosition();
        UUID worldUuid = player.getWorldUuid();
        World world = Universe.get().getWorld(worldUuid);

        world.execute(() -> {
                EventChat.UpdatePositionsAndChat(world.getEntityStore(), KweebecRegistry.getAll(), playerPos, player.getHeadRotation(), player, message);
        });
    }
}