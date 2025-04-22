package com.firesoul.collisiontest.model.impl;

import java.awt.Image;
import java.util.Optional;

import com.firesoul.collisiontest.model.api.Collider;
import com.firesoul.collisiontest.model.api.GameObject;
import com.firesoul.collisiontest.model.util.Vector2;

public class GameObjectImpl implements GameObject {

    private final Optional<Image> image;
    private final Optional<Collider> collider;
    
    private Vector2 position;
    private double orientation;

    public GameObjectImpl(final Vector2 position, final double orientation, final Optional<Collider> collider, final Optional<Image> image) {
        this.position = position;
        this.orientation = orientation;
        this.image = image;
        this.collider = collider;
    }

    @Override
    public void update(final double deltaTime) {
        
    }

    @Override
    public void rotate(final double angle) {
        this.orientation = this.orientation + angle;
        if (this.collider.isPresent()) {
            this.collider.get().rotate(angle);
        }
    }

    @Override
    public void move(final Vector2 position) {
        this.position = this.position.add(position);
        if (this.collider.isPresent()) {
            this.collider.get().move(position);
        }
    }

    @Override
    public Vector2 getPosition() {
        return this.position;
    }

    @Override
    public double getOrientation() {
        return this.orientation;
    }

    @Override
    public Optional<Image> getImage() {
        return this.image;
    }

    @Override
    public Optional<Collider> getCollider() {
        return this.collider;
    }

    @Override
    public void setSolid(final boolean solid) {
        if (this.collider.isEmpty()) {
            throw new IllegalStateException("Game object doesn't have a collider attached");
        }
        this.collider.get().setSolid(solid);
    }
}
