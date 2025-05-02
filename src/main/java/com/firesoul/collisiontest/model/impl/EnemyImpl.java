package com.firesoul.collisiontest.model.impl;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import com.firesoul.collisiontest.model.api.Collider;
import com.firesoul.collisiontest.model.api.Enemy;
import com.firesoul.collisiontest.model.util.Vector2;
import com.firesoul.collisiontest.view.api.Drawable;

public class EnemyImpl extends EntityImpl implements Enemy {

    private final Supplier<Void> behavior;
    private final Map<String, Drawable> sprites;

    public EnemyImpl(final Vector2 position, final double orientation, final boolean dynamic, final Optional<Collider> collider,
            final Map<String, Drawable> sprites, final int iframes, final int life, final Supplier<Void> behavior) {
        super(position, orientation, dynamic, collider, Optional.of(sprites.get("idle")), iframes, life);
        this.sprites = sprites;
        this.behavior = behavior;
    }

    @Override
    public void behave() {
        behavior.get();
    }

    @Override
    public void update(double deltaTime) {
        super.update(deltaTime);

        this.sprites.forEach((k, v) -> v.translate(this.getPosition()));
        this.behave();
        if (this.isInvincible()) {
            this.setSprite(this.sprites.get("damage"));
        } else {
            this.setSprite(this.sprites.get("idle"));
        }
    }
}
