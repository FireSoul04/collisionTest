package com.firesoul.collisiontest.model.impl;

import java.awt.Image;
import java.util.Optional;

import com.firesoul.collisiontest.model.api.Collider;
import com.firesoul.collisiontest.model.api.GameObject;
import com.firesoul.collisiontest.model.util.Vector2;

public class GameObjectImpl implements GameObject {

    private final boolean dynamic;

    private final Optional<Image> image;
    private final Optional<Collider> collider;
    
    private Vector2 position;
    private Vector2 velocity;
    private double orientation;
    private boolean active;

    public GameObjectImpl(final Vector2 position, final double orientation, final boolean dynamic, final Optional<Collider> collider, final Optional<Image> image) {
        this.position = position;
        this.orientation = orientation;
        this.dynamic = dynamic;
        this.image = image;
        this.collider = collider;
        this.velocity = Vector2.zero();
        this.active = true;
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
    public Vector2 getVelocity() {
        return this.velocity;
    }
    
    @Override
    public void setVelocity(final Vector2 velocity) {
        this.velocity = velocity;
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
    public boolean isStatic() {
        return !this.dynamic;
    }

    @Override
    public boolean isDynamic() {
        return this.dynamic;
    }

    @Override
    public void setSolid(final boolean solid) {
        if (this.collider.isEmpty()) {
            throw new IllegalStateException("Game object doesn't have a collider attached");
        }
        this.collider.get().setSolid(solid);
    }

    @Override
    public boolean isActive() {
        return this.active;
    }

    @Override
    public void destroy() {
        this.active = false;
    }
}
