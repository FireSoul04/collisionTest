package com.firesoul.collisiontest.model.impl.gameobjects.weapons;

import com.firesoul.collisiontest.model.api.GameObject;
import com.firesoul.collisiontest.model.api.Level;
import com.firesoul.collisiontest.model.api.factories.GameObjectFactory;
import com.firesoul.collisiontest.model.impl.gameobjects.bars.GameBar;
import com.firesoul.collisiontest.model.util.GameTimer;
import com.firesoul.collisiontest.model.util.Vector2;
import com.firesoul.collisiontest.model.api.Drawable;

import java.util.Optional;

public class Gun extends WeaponImpl {

    private final GameObjectFactory gf;
    private final GameBar reloadBar;
    private final GameTimer shootCooldown;
    private final Vector2 projectileOffset;
    private final Vector2 projectileVelocity;

    private final GameTimer reloadTimer;
    private final int maxProjectiles;
    private int projectiles;

    public Gun(
        final GameObject holder,
        final GameBar reloadBar,
        final Vector2 offset,
        final Vector2 projectileOffset,
        final Optional<Drawable> sprite,
        final Level world,
        final GameObjectFactory gf,
        final int maxProjectiles
    ) {
        super(holder, offset, offset, world, Optional.empty(), sprite);
        this.gf = gf;
        this.reloadBar = reloadBar;
        this.shootCooldown = new GameTimer(400);
        this.projectileOffset = projectileOffset;
        this.maxProjectiles = maxProjectiles;
        this.reloadTimer = new GameTimer (
            () -> {
                this.projectiles = this.maxProjectiles;
                reloadBar.getSprite().ifPresent(s -> s.setVisible(false));
            },
            (r, d) -> reloadBar.setCurrentPercentage(r / (double) d),
            1500
        );
        this.projectiles = this.maxProjectiles;
        this.projectileVelocity = Vector2.right().multiply(10.0);
    }

    @Override
    public void attack() {
        if (!this.shootCooldown.isRunning() && !this.reloadTimer.isRunning()) {
            this.shootCooldown.start();
            this.projectiles--;

            this.gf.projectile(
                this.getHolder().getPosition().add(
                    this.projectileOffset.multiply(new Vector2(this.getDirectionX(), 0.0))
                ),
                this.projectileVelocity.x() * this.getDirectionX()
            );
        }

        if (this.projectiles == 0) {
            this.reload();
        }
    }

    public void reload() {
        if (this.projectiles < this.maxProjectiles && !this.reloadTimer.isRunning()) {
            this.reloadTimer.start();
            this.reloadBar.getSprite().ifPresent(s -> s.setVisible(true));
        }
    }
}
