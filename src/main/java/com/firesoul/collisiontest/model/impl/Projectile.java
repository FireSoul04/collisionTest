package com.firesoul.collisiontest.model.impl;

import java.awt.Image;
import java.util.Optional;

import com.firesoul.collisiontest.model.api.Collider;
import com.firesoul.collisiontest.model.util.Vector2;

public class Projectile extends GameObjectImpl {

    private final double speed;

    public Projectile(final Vector2 position, final double orientation, final Optional<Collider> collider, final Optional<Image> image, final double speed) {
        super(position, orientation, collider, image);
        this.speed = speed;
    }
    
    @Override
    public void update(final double deltaTime) {
        this.setVelocity(Vector2.right().multiply(this.speed));
        this.move(this.getVelocity().multiply(deltaTime));
    }
}
