package com.firesoul.collisiontest.model.impl;

import java.util.Objects;
import java.util.Optional;

import com.firesoul.collisiontest.model.api.physics.Collider;
import com.firesoul.collisiontest.model.api.GameObject;
import com.firesoul.collisiontest.model.api.GameObjectBuilder;
import com.firesoul.collisiontest.model.api.Level;
import com.firesoul.collisiontest.model.impl.gameobjects.GameObjectImpl;
import com.firesoul.collisiontest.model.util.Vector2;
import com.firesoul.collisiontest.view.api.Drawable;

public class GameObjectBuilderImpl implements GameObjectBuilder {

    private final Vector2 position;
    private final boolean dynamic;
    private final Level world;
    private Optional<Collider> collider;
    private Optional<Drawable> sprite;

    public GameObjectBuilderImpl(final Vector2 position, final boolean dynamic, final Level world) {
        Objects.requireNonNull(position);
        this.position = position;
        this.dynamic = dynamic;
        this.world = world;
        this.sprite = Optional.empty();
    }

    @Override
    public GameObjectBuilder collider(final Collider collider) {
        Objects.requireNonNull(collider);
        this.collider = Optional.of(collider);
        return this;
    }

    @Override
    public GameObjectBuilder sprite(final Drawable sprite) {
        Objects.requireNonNull(sprite);
        this.sprite = Optional.of(sprite);
        return this;
    }

    @Override
    public GameObject build() {
        this.collider.ifPresent(t -> t.setPosition(this.position));
        return new GameObjectImpl(this.position, this.dynamic, this.world, this.collider, this.sprite);
    }

    protected Vector2 getPosition() {
        return this.position;
    }

    protected Optional<Collider> getCollider() {
        return this.collider;
    }

    protected Optional<Drawable> getSprite() {
        return this.sprite;
    }
}
