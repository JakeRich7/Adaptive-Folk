package com.adaptivefolk;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;

public class Chat {

    public static void EchoPlayer(PlayerChatEvent event) {

        String message = event.getContent();
        PlayerRef player = event.getSender();
        player.sendMessage(Message.raw("I am echoing this message: " + message));
    }
}