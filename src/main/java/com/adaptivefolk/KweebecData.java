package com.adaptivefolk;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.UUID;

public class KweebecData {

    private final Ref<EntityStore> reference;
    private Vector3d position;

    public KweebecData(Ref<EntityStore> reference, Vector3d position) {
        this.reference = reference;
        this.position = position;
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

    @Override
    public String toString() {
        return "KweebecData{position=" + position + "}";
    }
}