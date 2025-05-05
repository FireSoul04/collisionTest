package com.firesoul.collisiontest.model.impl.physics;

import com.firesoul.collisiontest.model.api.physics.RigidBody;
import com.firesoul.collisiontest.model.util.Vector2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class EnhancedRigidBody implements RigidBody {

    private static final double EPSILON = 1e-1;

    private final List<Vector2> forces = new ArrayList<>();

    private final Vector2 friction;
    private final Vector2 gravity;
    private final Vector2 maxVelocity;

    private Vector2 velocity = Vector2.zero();

    public EnhancedRigidBody(final Vector2 maxVelocity, final Vector2 gravity, final Vector2 friction) {
        this.friction = friction;
        this.gravity = gravity;
        this.maxVelocity = maxVelocity;
    }

    public EnhancedRigidBody(final Vector2 maxVelocity) {
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
        this.applyForce(direction);
    }

    private void applyFriction() {
        if (this.velocity.norm() > EPSILON) {
            this.applyForce(this.friction.multiply(this.velocity.invert().normalize()));
        } else {
            this.applyForce(this.velocity.invert());
        }

        if (Math.abs(this.velocity.x()) <= this.maxVelocity.x()) {
            this.applyForce(this.velocity.invert().multiply(this.friction));
        }
    }

    private void addForce(final Vector2 force) {
        this.velocity = this.velocity.add(force);
    }
}
