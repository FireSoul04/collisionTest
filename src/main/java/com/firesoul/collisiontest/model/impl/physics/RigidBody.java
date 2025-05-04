package com.firesoul.collisiontest.model.impl.physics;

import com.firesoul.collisiontest.model.api.physics.PhysicsBody;
import com.firesoul.collisiontest.model.util.Vector2;

import java.util.ArrayList;
import java.util.List;

public class RigidBody implements PhysicsBody {

    private final List<Vector2> forces = new ArrayList<>();

    private final Vector2 maxFriction = Vector2.one();
    private final Vector2 frictionStep;
    private final Vector2 gravity;

    private Vector2 friction;

    private Vector2 velocity = Vector2.zero();

    private final Vector2 maxVelocity;
    private Vector2 currentVelocity = Vector2.zero();

    public RigidBody(final Vector2 maxVelocity, final Vector2 gravity, final Vector2 frictionStep) {
        this.frictionStep = frictionStep;
        this.friction = this.maxFriction;
        this.maxVelocity = maxVelocity;
        this.gravity = gravity;
    }

    public RigidBody(final Vector2 maxVelocity) {
        this(maxVelocity, new Vector2(0.0, 0.25), new Vector2(0.0625, 0.0));
    }

    @Override
    public void update() {
        this.applyFriction();
        this.applyForce(this.gravity);
        this.forces.forEach(this::addForce);
        this.forces.clear();
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
    public void applyForce(final Vector2 force) {
        this.forces.add(force);
    }

    @Override
    public void move(final Vector2 direction) {
        if (direction.x() != 0.0 && this.currentVelocity.x() < this.maxVelocity.x()) {
            this.currentVelocity = this.currentVelocity.add(direction.normalize());
        } else if (direction.x() == 0.0) {
            this.currentVelocity = new Vector2(0.0, this.currentVelocity.y());
        }
        this.applyForce(direction);
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

    private void addForce(final Vector2 force) {
        this.velocity = this.velocity.add(force);
    }
}
