package com.firesoul.collisiontest.model.impl;

import com.firesoul.collisiontest.model.api.GameObject;
import com.firesoul.collisiontest.model.util.Vector2;

public class ProjectileBuilder extends GameObjectBuilderImpl {

    private final double speed;

    public ProjectileBuilder(final Vector2 position, final double speed) {
        super(position, true);
        if (!Double.isFinite(speed)) {
            throw new IllegalStateException("Projectile speed must be a number");
        }
        this.speed = speed;
    }

    @Override
    public GameObject build() {
        final GameObject builded = new Projectile(this.getPosition(), this.getOrientation(), this.getCollider(), this.getImage(), this.speed);
        if (this.getCollider().isPresent()) {
            this.getCollider().get().attachGameObject(builded);
        }
        return builded;
    }
}
