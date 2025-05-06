package com.firesoul.collisiontest.model.impl.gameobjects;

import java.util.Objects;
import java.util.Optional;

import com.firesoul.collisiontest.model.api.physics.Collider;
import com.firesoul.collisiontest.model.api.GameObject;
import com.firesoul.collisiontest.model.api.Level;
import com.firesoul.collisiontest.model.util.Vector2;
import com.firesoul.collisiontest.model.api.Drawable;

public class GameObjectImpl implements GameObject {

    private final Level world;
    private final boolean dynamic;

    private final Optional<Collider> collider;
    private Optional<Drawable> sprite;
    
    private Vector2 position;
    private Vector2 velocity;
    private double orientation;
    private boolean active;

    public GameObjectImpl(final Vector2 position, final boolean dynamic, final Level world, final Optional<Collider> collider, final Optional<Drawable> sprite) {
        this.position = position;
        this.orientation = 0.0;
        this.world = world;
        this.dynamic = dynamic;
        this.sprite = sprite;
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
        this.collider.ifPresent(value -> value.rotate(angle));
        this.sprite.ifPresent(drawable -> drawable.rotate(angle));
    }

    @Override
    public void move(final Vector2 position) {
        this.position = this.position.add(position);
        this.collider.ifPresent(value -> value.move(position));
        this.sprite.ifPresent(drawable -> drawable.translate(this.position));
    }

    @Override
    public Vector2 getPosition() {
        return this.position;
    }

    @Override
    public void setPosition(final Vector2 position) {
        this.position = position;
        this.collider.ifPresent(value -> value.setPosition(position));
        this.sprite.ifPresent(drawable -> drawable.translate(this.position));
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
    public Optional<Drawable> getSprite() {
        return this.sprite;
    }

    protected void setSprite(final Drawable sprite) {
        Objects.requireNonNull(sprite);
        this.sprite = Optional.of(sprite);
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
    public void onDestroy() {

    }

    @Override
    public void destroy() {
        this.onDestroy();
        this.active = false;
    }

    protected Level getWorld() {
        return this.world;
    }
}
