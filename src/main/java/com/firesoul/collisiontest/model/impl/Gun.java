package com.firesoul.collisiontest.model.impl;

import com.firesoul.collisiontest.model.api.Collider;
import com.firesoul.collisiontest.model.api.GameObject;
import com.firesoul.collisiontest.model.util.GameTimer;
import com.firesoul.collisiontest.model.util.Vector2;
import com.firesoul.collisiontest.view.api.Drawable;

import java.util.Optional;

public class Gun extends WeaponImpl {

    private final GameCollisions world;
    private final GameTimer shootCooldown;
    private final Vector2 projectileOffset;

    private final GameTimer reloadTimer;
    private final int maxProjectiles;
    private int projectiles;

    public Gun(
        final GameObject holder,
        final Vector2 offset,
        final Vector2 projectileOffset,
        final double orientation,
        final Optional<Drawable> sprite,
        final GameCollisions world,
        final int maxProjectiles
    ) {
        super(holder, offset, offset, orientation, Optional.empty(), sprite);
        this.shootCooldown = new GameTimer(() -> {}, 0, 400);
        this.world = world;
        this.projectileOffset = projectileOffset;
        this.maxProjectiles = maxProjectiles;
        this.reloadTimer = new GameTimer(() -> this.projectiles = this.maxProjectiles, 0, 2000);
        this.projectiles = this.maxProjectiles;
    }

    @Override
    public void attack() {
        if (!this.shootCooldown.isRunning() && !this.reloadTimer.isRunning()) {
            this.shootCooldown.start();
            this.projectiles--;
            this.world.spawnProjectile(this.getPosition().add(projectileOffset));
        }

        if (this.projectiles == 0) {
            this.reload();
        }
    }

    public void reload() {
        if (this.projectiles < this.maxProjectiles && !this.reloadTimer.isRunning()) {
            this.reloadTimer.start();
        }
    }
}
