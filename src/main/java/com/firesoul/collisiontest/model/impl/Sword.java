package com.firesoul.collisiontest.model.impl;

import com.firesoul.collisiontest.model.api.Collider;
import com.firesoul.collisiontest.model.api.Enemy;
import com.firesoul.collisiontest.model.api.GameObject;
import com.firesoul.collisiontest.model.api.Weapon;
import com.firesoul.collisiontest.model.util.GameTimer;
import com.firesoul.collisiontest.model.util.Vector2;
import com.firesoul.collisiontest.view.api.Drawable;

import java.util.Map;
import java.util.Optional;

public class Sword extends GameObjectImpl implements Weapon {

    private final GameObject holder;
    private final Vector2 offset;
    private final Vector2 spriteOffset;
    private final Map<String, Drawable> sprites;

    private Vector2 update = Vector2.zero();
    private double angle = 0.0;
    private final double step = 0.25;
    private final double range = 8.0;

    private final GameTimer swingTimer;

    public Sword(final GameObject holder, final Vector2 offset, final Vector2 spriteOffset, final double orientation, final Collider collider, final Map<String, Drawable> sprites) {
        super(holder.getPosition().add(offset), orientation, true, Optional.of(collider), Optional.of(sprites.get("idle")));
        this.holder = holder;
        this.offset = offset;
        this.spriteOffset = spriteOffset;
        this.sprites = sprites;
        this.swingTimer = new GameTimer(() -> {
            this.setSolid(false);
            this.setSprite(this.sprites.get("idle"));
        }, 0, 200);
        this.getCollider().ifPresent(t -> t.setSolid(false));
    }

    @Override
    public void onCollide(final Collider collidedShape, final Vector2 collisionDirection, final double collisionTime) {
        final GameObject g = collidedShape.getAttachedGameObject();
        if (g instanceof Enemy e && this.getCollider().isPresent() && this.getCollider().get().isSolid()) {
            e.takeDamage(3);
        }
    }

    @Override
    public void update(final double deltaTime) {
        this.setPosition(this.holder.getPosition().add(this.offset).add(this.update));
        this.getSprite().ifPresent(t -> t.translate(this.holder.getPosition().add(this.spriteOffset)));

        if (this.swingTimer.isRunning()) {
            this.update = this.update.add(new Vector2(Math.cos(this.angle), Math.sin(this.angle)).multiply(this.range));
            this.angle += this.step;
        } else {
            this.angle = 0;
            this.update = Vector2.zero();
        }
    }

    @Override
    public void attack() {
        if (!this.swingTimer.isRunning()) {
            this.swingTimer.start();
            this.setSolid(true);
            this.setSprite(this.sprites.get("swing"));
        }
    }

    @Override
    public GameObject getHolder() {
        return this.holder;
    }
}
