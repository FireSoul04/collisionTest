package com.firesoul.collisiontest.model.impl.gameobjects.weapons;

import com.firesoul.collisiontest.model.api.Collider;
import com.firesoul.collisiontest.model.api.Level;
import com.firesoul.collisiontest.model.api.gameobjects.Enemy;
import com.firesoul.collisiontest.model.api.GameObject;
import com.firesoul.collisiontest.model.impl.CollisionAlgorithms;
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

    private final GameTimer swingTimer;
    private final GameTimer swingCooldown;

    public Sword(
            final GameObject holder,
            final Vector2 offset,
            final Vector2 spriteOffset,
            final Level world,
            final Collider collider,
            final Map<String, Drawable> sprites
    ) {
        super(holder, offset, spriteOffset, world, Optional.of(collider), Optional.of(sprites.get("idle")));
        this.sprites = sprites;
        this.swingTimer = new GameTimer(() -> {
            this.setSolid(false);
            this.setSprite(this.sprites.get("idle"));
        }, 0, 150);
        this.swingCooldown = new GameTimer(() -> {}, 0, 400);
        this.getCollider().ifPresent(t -> t.setSolid(false));
    }

    @Override
    public void onCollision(final GameObject gameObject, final Vector2 collisionDirection, final double collisionTime) {
        if (gameObject instanceof Enemy e && this.getCollider().isPresent() && this.getCollider().get().isSolid()) {
            e.takeDamage(3);

            final var r1 = CollisionAlgorithms.fitInRect(this.getCollider().get());
            final var r2 = CollisionAlgorithms.fitInRect(gameObject.getCollider().orElseThrow());
            final double distX = Math.signum((this.getPosition().x() + r1.w()/2.0) - (gameObject.getPosition().x() + r2.w()/2.0));
            this.getHolder().setVelocity(new Vector2(distX*5, this.getVelocity().y()));
        }
    }

    @Override
    public void update(final double deltaTime) {
        final Vector2 position = new Vector2(
                this.getSpriteOffset().x() * this.getDirectionX(),
                this.getSpriteOffset().y()
        );
        this.setPosition(this.getHolder().getPosition().add(this.getOffset()));
        this.getCollider().ifPresent(t -> t.setPosition(
            this.getHolder().getPosition()
                .add(new Vector2(this.getOffset().x() * this.getDirectionX(), this.getOffset().y()))
                .add(new Vector2(this.update.x() * this.getDirectionX(), this.update.y())))
        );
        this.sprites.forEach((k, v) -> {
            v.translate(this.getHolder().getPosition().add(position));
            v.mirrorX(this.getDirectionX());
        });

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
        if (!this.swingTimer.isRunning() && !this.swingCooldown.isRunning()) {
            this.swingTimer.start();
            this.swingCooldown.start();
            this.setSolid(true);
            this.setSprite(this.sprites.get("swing"));
        }
    }
}
