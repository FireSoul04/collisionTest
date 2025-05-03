package com.firesoul.collisiontest.model.impl;

import com.firesoul.collisiontest.model.api.Collider;
import com.firesoul.collisiontest.model.api.Enemy;
import com.firesoul.collisiontest.model.api.GameObject;
import com.firesoul.collisiontest.model.util.GameTimer;
import com.firesoul.collisiontest.model.util.Vector2;
import com.firesoul.collisiontest.view.api.Drawable;

import java.util.Map;
import java.util.Optional;

public class Sword extends WeaponImpl {

    private final Map<String, Drawable> sprites;

    private Vector2 update = Vector2.zero();
    private double angle = 0.0;
    private final double step = 0.25;
    private final double range = 4.0;

    private final GameTimer swingCooldown;

    public Sword(
        final GameObject holder,
        final Vector2 offset,
        final Vector2 spriteOffset,
        final double orientation,
        final Collider collider,
        final Map<String, Drawable> sprites
    ) {
        super(holder, offset, spriteOffset, orientation, Optional.of(collider), Optional.of(sprites.get("idle")));
        this.sprites = sprites;
        this.swingCooldown = new GameTimer(() -> {
            this.setSolid(false);
            this.setSprite(this.sprites.get("idle"));
        }, 0, 200);
        this.getCollider().ifPresent(t -> t.setSolid(false));
    }

    @Override
    public void onCollision(final Collider collidedShape, final Vector2 collisionDirection, final double collisionTime) {
        final GameObject g = collidedShape.getAttachedGameObject();
        if (g instanceof Enemy e && this.getCollider().isPresent() && this.getCollider().get().isSolid()) {
            e.takeDamage(3);

            final var r1 = CollisionAlgorithms.fitInRect(this.getCollider().get());
            final var r2 = CollisionAlgorithms.fitInRect(collidedShape);
            final double distX = Math.signum((this.getPosition().x() + r1.w()/2.0) - (g.getPosition().x() + r2.w()/2.0));
            this.getHolder().setVelocity(new Vector2(distX*5, this.getVelocity().y()));
        }
    }

    @Override
    public void update(final double deltaTime) {
        super.update(deltaTime);
        this.getCollider().ifPresent(t -> t.setPosition(
            this.getHolder().getPosition()
                .add(new Vector2(this.getOffset().x() * this.getDirectionX(), this.getOffset().y()))
                .add(new Vector2(this.update.x() * this.getDirectionX(), this.update.y())))
        );
        if (this.swingCooldown.isRunning()) {
            this.update = this.update.add(new Vector2(Math.cos(this.angle), Math.sin(this.angle)).multiply(this.range));
            this.angle += this.step;
        } else {
            this.angle = 0;
            this.update = Vector2.zero();
        }
    }

    @Override
    public void attack() {
        if (!this.swingCooldown.isRunning()) {
            this.swingCooldown.start();
            this.setSolid(true);
            this.setSprite(this.sprites.get("swing"));
        }
    }
}
