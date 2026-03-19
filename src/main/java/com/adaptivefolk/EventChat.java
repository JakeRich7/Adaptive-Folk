package com.adaptivefolk;

import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.component.Ref;

import java.util.Map;
import java.util.UUID;

import static java.lang.Math.PI;

public class EventChat {
    private static final double CHAT_DISTANCE = 5.0;
    private static final int MAX_FACING = 90;

    public static void UpdatePositionsAndChat(EntityStore store, Map<Ref<EntityStore>, KweebecData> kweebecs, Vector3d playerPos, Vector3f playerHeadRotation, PlayerRef player, String playerMessage) {
        KweebecData closest = null;
        double closestDistance = Double.MAX_VALUE;

        // Update all Kweebec positions and conditionally compare with player
        for (Map.Entry<Ref<EntityStore>, KweebecData> entry : kweebecs.entrySet()) {
            Ref<EntityStore> ref = entry.getKey();
            KweebecData data = entry.getValue();

            TransformComponent transform = (TransformComponent) store.getStore().getComponent(ref, TransformComponent.getComponentType());
            if (transform != null) {
                data.setPosition(transform.getPosition());
            }

            if (data.getPosition() != null) {
                double dx = data.getPosition().x - playerPos.x;
                double dz = data.getPosition().z - playerPos.z;
                double distanceXZ = Math.sqrt(dx * dx + dz * dz);

                // If Kweebec position is within player distance perform player head check
                if (distanceXZ <= CHAT_DISTANCE) {
                    double playerYaw = playerHeadRotation.y;
                    double angleToKweebec = Math.atan2(dx, dz);
                    double delta = normalizeRadians(angleToKweebec - playerYaw);
                    delta = ((delta + PI) % (2 * PI)) - PI;
                    double absDelta = Math.abs(delta);
                    double maxFacing = PI - (Math.toRadians(MAX_FACING) / 2);

                    if (distanceXZ < closestDistance && absDelta >= maxFacing) {
                        closestDistance = distanceXZ;
                        closest = data;
                    }
                }
            }
        }

        // Chat with Kweebec if within player distance and player view
        String npcName = closest.getName();
        UUID npcUUID = closest.getUuid();
        KweebecStorage.appendMessage(npcUUID, "player", playerMessage);

        KweebecAiResponse.getResponseAsync(playerMessage, npcName, npcUUID)
                .thenAccept(aiResponse -> {
                    // ai response
                    String rawResponse = aiResponse.strip();
                    String response = npcName + ": " + rawResponse;
                    player.sendMessage(Message.raw(response));
                    System.out.println(response);
                    KweebecStorage.appendMessage(npcUUID, npcName, rawResponse);
                })
                .exceptionally(e -> {
                    // fallback response if AI fails
                    String rawResponse = KweebecFallbackResponse.getResponse(npcName);
                    String response = npcName + ": " + rawResponse;
                    player.sendMessage(Message.raw(response));
                    System.out.println(response);
                    KweebecStorage.appendMessage(npcUUID, npcName, rawResponse);
                    return null;
                });
        }

    private static double normalizeRadians(double angle) {
        angle = angle % (2 * PI);
        if (angle > PI) angle -= 2 * PI;
        if (angle < -PI) angle += 2 * PI;
        return angle;
    }
}