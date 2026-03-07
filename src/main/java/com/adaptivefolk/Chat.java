package com.adaptivefolk;

import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;

public class Chat {
    public static void EchoPlayer(PlayerChatEvent event) {
        String message = event.getContent();
        PlayerRef player = event.getSender();
        Vector3d playerPos = player.getTransform().getPosition();

        player.sendMessage(Message.raw("I am echoing this message: " + message));
        System.out.println("Player is at: " + playerPos);
    }
}