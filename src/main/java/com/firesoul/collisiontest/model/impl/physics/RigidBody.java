package com.firesoul.collisiontest.model.impl.physics;

import com.firesoul.collisiontest.model.api.physics.PhysicsBody;
import com.firesoul.collisiontest.model.util.Vector2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class RigidBody implements PhysicsBody {

    private final List<Vector2> forces = new ArrayList<>();

    private final Vector2 friction;
    private final Vector2 gravity;

    private Vector2 velocity = Vector2.zero();

    private final Vector2 maxVelocity;

    public RigidBody(final Vector2 maxVelocity, final Vector2 gravity, final Vector2 friction) {
        this.friction = friction;
        this.maxVelocity = maxVelocity;
        this.gravity = gravity;
    }

    public RigidBody(final Vector2 maxVelocity) {
        this(maxVelocity, new Vector2(0.0, 0.25), new Vector2(0.0625, 0.0));
    }

    public List<Vector2> forcesDebug = new CopyOnWriteArrayList<>();

    @Override
    public void update() {
        this.applyFriction();
        this.applyForce(this.gravity);
        this.forces.forEach(this::addForce);
        
        forcesDebug.addAll(forces);
        
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
        if (this.velocity.norm() <= this.maxVelocity.norm()) {
            this.applyForce(direction);
        }
    }

    private void applyFriction() {
        this.applyForce(this.friction.multiply(this.velocity.invert().normalize()));
    }

    private void addForce(final Vector2 force) {
        this.velocity = this.velocity.add(force);
    }
}
