package com.adaptivefolk;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import java.util.UUID;

public class KweebecData {
    private final Ref<EntityStore> reference;
    private Vector3d position;
    private final String name;
    private final String roleName;
    private final UUID uuid;

    public KweebecData(Ref<EntityStore> reference, Vector3d position, String name, String roleName, UUID uuid) {
        this.reference = reference;
        this.position = position;
        this.name = name;
        this.roleName = roleName;
        this.uuid = uuid;
    }

    public Ref<EntityStore> getReference() {
        return reference;
    }

    public Vector3d getPosition() {
        return position;
    }

    public void setPosition(Vector3d position) {
        this.position = position;
    }

    public String getName() { return name; }

    public String getRoleName() { return roleName; }

    public UUID getUuid() { return uuid; }

    @Override
    public String toString() { return "KweebecData{name=" + name + ", uuid=" + uuid + ", position=" + position + "}"; }
}