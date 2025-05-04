package com.firesoul.collisiontest.model.impl.gameobjects;

import com.firesoul.collisiontest.model.api.gameobjects.PhysicsBody;
import com.firesoul.collisiontest.model.util.Vector2;

public class EnhancedPhysicsBody implements PhysicsBody {

    private final Vector2 maxFriction = Vector2.one();
    private final Vector2 frictionStep;
    private final Vector2 gravity;

    private Vector2 friction;

    private Vector2 velocity = Vector2.zero();

    private final Vector2 maxVelocity;
    private Vector2 currentVelocity = Vector2.zero();

    public EnhancedPhysicsBody(final Vector2 gravity, final Vector2 frictionStep, final Vector2 maxVelocity) {
        this.gravity = gravity;
        this.frictionStep = frictionStep;
        this.friction = this.maxFriction;
        this.maxVelocity = maxVelocity;
    }

    @Override
    public void update() {
        this.applyFriction();
        this.applyVelocity(this.gravity);
    }

    @Override
    public Vector2 getVelocity() {
        return this.velocity;
    }

    @Override
    public void setVelocity(final Vector2 velocity) {
        this.velocity = velocity;
    }

    @Override
    public void applyVelocity(final Vector2 velocity) {
        this.velocity = this.velocity.add(velocity);
    }

    @Override
    public void move(Vector2 direction) {
        if (direction.x() != 0.0 && this.currentVelocity.x() < this.maxVelocity.x()) {
            this.currentVelocity = this.currentVelocity.add(direction.normalize());
        } else if (direction.x() == 0.0) {
            this.currentVelocity = new Vector2(0.0, this.currentVelocity.y());
        }
        this.applyVelocity(direction);
    }

    private void applyFriction() {
        if (this.velocity.norm() > 0.0 && this.currentVelocity.norm() == 0.0) {
            if (this.friction.norm() > 0.0) {
                this.friction = this.friction.subtract(this.frictionStep);
            }
        } else {
            this.friction = this.maxFriction;
        }
        this.velocity = this.velocity.multiply(this.friction);
    }
}
