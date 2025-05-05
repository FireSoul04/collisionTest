package com.firesoul.collisiontest.model.impl.gameobjects;

import java.util.Optional;

import com.firesoul.collisiontest.model.api.physics.Collider;
import com.firesoul.collisiontest.model.api.Level;
import com.firesoul.collisiontest.model.api.gameobjects.Enemy;
import com.firesoul.collisiontest.model.api.GameObject;
import com.firesoul.collisiontest.model.api.physics.RigidBody;
import com.firesoul.collisiontest.model.impl.physics.EnhancedRigidBody;
import com.firesoul.collisiontest.model.util.Vector2;
import com.firesoul.collisiontest.view.api.Drawable;

public class Projectile extends GameObjectImpl {

    private final RigidBody body;

    public Projectile(final Vector2 position, final Level world, final Optional<Collider> collider,
                      final Optional<Drawable> sprite, final double speed
    ) {
        super(position, true, world, collider, sprite);
        this.body = new EnhancedRigidBody(Vector2.zero(), Vector2.zero(), Vector2.zero());
        this.setVelocity(new Vector2(speed, 0.0));
    }

    @Override
    public Vector2 getVelocity() {
        return this.body.getVelocity();
    }

    @Override
    public void setVelocity(final Vector2 velocity) {
        this.body.setVelocity(velocity);
    }
    
    @Override
    public void update(final double deltaTime) {
        this.body.update();
        this.move(this.getVelocity().multiply(deltaTime));
    }

    @Override
    public void onCollision(final GameObject gameObject, final Vector2 collisionDirection, final double collisionTime) {
        if (gameObject instanceof Enemy e) {
            e.takeDamage(3);
            this.destroy();
        }
    }
}
