package com.adaptivefolk;

import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;

import java.util.UUID;

public class Chat {
    public static void EchoPlayer(PlayerChatEvent event) {
        String message = event.getContent();
        PlayerRef player = event.getSender();
        Vector3d playerPos = player.getTransform().getPosition();

        UUID worldUuid = player.getWorldUuid();
        World world = Universe.get().getWorld(worldUuid);

        player.sendMessage(Message.raw("I am echoing this message: " + message));
//        System.out.println("Player is at: " + playerPos);

        world.execute(() -> {
                KweebecUtils.updateKweebecPositions(world.getEntityStore(), KweebecRegistry.getAll(), playerPos, player.getHeadRotation());
        });
    }
}