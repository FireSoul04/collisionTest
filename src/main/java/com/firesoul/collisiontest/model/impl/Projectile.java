package com.firesoul.collisiontest.model.impl;

import java.util.Optional;

import com.firesoul.collisiontest.model.api.Collider;
import com.firesoul.collisiontest.model.api.Enemy;
import com.firesoul.collisiontest.model.api.GameObject;
import com.firesoul.collisiontest.model.util.Vector2;
import com.firesoul.collisiontest.view.api.Drawable;

public class Projectile extends GameObjectImpl {

    private final double speed;

    public Projectile(final Vector2 position, final double orientation, final Optional<Collider> collider, final Optional<Drawable> sprite, final double speed) {
        super(position, orientation, true, collider, sprite);
        this.speed = speed;
    }
    
    @Override
    public void update(final double deltaTime) {
        this.setVelocity(Vector2.right().multiply(this.speed));
        this.move(this.getVelocity().multiply(deltaTime));
    }

    @Override
    public void onCollide(final Collider collidedShape, final Vector2 collisionDirection, final double collisionTime) {
        final GameObject g = collidedShape.getAttachedGameObject();
        if (g instanceof Enemy e) {
            this.destroy();
            e.takeDamage(3);
        }
    }
}
