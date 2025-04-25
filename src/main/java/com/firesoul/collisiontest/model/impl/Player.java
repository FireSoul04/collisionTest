package com.firesoul.collisiontest.model.impl;

import java.awt.Image;
import java.util.Optional;

import com.firesoul.collisiontest.controller.impl.InputController;
import com.firesoul.collisiontest.model.api.Collider;
import com.firesoul.collisiontest.model.api.GameObject;
import com.firesoul.collisiontest.model.impl.BlockBuilder.Block;
import com.firesoul.collisiontest.model.util.Vector2;
import com.firesoul.collisiontest.view.impl.Animation;
import com.firesoul.collisiontest.view.impl.SwordSwingAnimation;

public class Player extends GameObjectImpl {

    private final double speed = 0.05;
    private final double rotSpeed = 0.015;

    private final GameObject sword;
    private final GameCollisions world;
    private final InputController input;

    private final Animation swordSwingReset;
    private final Animation swordSwingEnd;
    private final Animation swordSwingStart;
    private boolean swinging = false;

    // Movement logic
    private double friction = 1.0;
    private int currentVelocity = 1;
    private int maxVelocity = 10;
    // Gravity logic
    private Vector2 gravityAcceleration = new Vector2(0.0, 0.25);
    // Jump logic
    private Vector2 jumpAcceleration = new Vector2(0.0, -0.06);
    private boolean onGround = false;
    private int currentJumpHeight = 0;
    private int maxJumpHeight = 20;

    public Player(
        final Vector2 position,
        final double orientation,
        final Optional<Collider> collider,
        final Optional<Image> image,
        final GameObject sword,
        final InputController input,
        final GameCollisions world
    ) {
        super(position, orientation, true, collider, image);

        this.sword = sword;
        this.input = input;
        this.world = world;
        this.swordSwingReset = new SwordSwingAnimation(
            2,
            () -> sword.rotate(-((Player) this).getRotationSpeed()*10),
            () -> sword.setSolid(false),
            () -> sword.getOrientation() < Math.PI/3
        );
        this.swordSwingEnd = new SwordSwingAnimation(
            10,
            () -> sword.rotate(((Player) this).getRotationSpeed()*10),
            () -> swordSwingReset.start(),
            () -> sword.getOrientation() > Math.PI*3/4
        );
        this.swordSwingStart = new SwordSwingAnimation(
            5,
            () -> sword.rotate(-((Player) this).getRotationSpeed()*2),
            () -> {
                swordSwingEnd.start();
                sword.setSolid(true); 
            },
            () -> sword.getOrientation() < Math.PI/12
        );
    }

    @Override
    public void update(final double deltaTime) {
        this.move(this.getVelocity().multiply(deltaTime));
        this.sword.move(this.getVelocity().multiply(deltaTime));
    }

    @Override
    public void onCollide(final Collider collidedShape, final Vector2 collisionDirection, final double collisionTime) {
        if (collidedShape.getAttachedGameObject() instanceof Block && collisionDirection.equals(new Vector2(0.0, -1.0))) {
            this.currentJumpHeight = 0;
            this.onGround = true;
        }
    }

    public double getSpeed() {
        return this.speed;
    }

    public double getRotationSpeed() {
        return this.rotSpeed;
    }

    public void readInput() {
        Vector2 velocity = this.inputMove();
        velocity = this.jump(velocity);
        velocity = this.applyFriction(velocity);
        velocity = this.applyGravity(velocity);
        this.onGround = false;

        this.swingSword();
        this.shoot();

        this.setVelocity(velocity);
    }

    private Vector2 inputMove() {
        Vector2 velocity = Vector2.zero();
        if (this.input.getEvent("MoveLeft")) {
            velocity = velocity.add(Vector2.left().multiply(this.currentVelocity));
        }
        if (this.input.getEvent("MoveRight")) {
            velocity = velocity.add(Vector2.right().multiply(this.currentVelocity));
        }
        if (velocity.x() != 0.0 && this.currentVelocity < this.maxVelocity) {
            this.currentVelocity++;
        } else if (velocity.x() == 0.0) {
            this.currentVelocity = 1;
        }
        velocity = velocity.multiply(this.speed);
        return velocity;
    }

    private Vector2 jump(final Vector2 velocity) {
        Vector2 newVelocity = velocity;
        if (this.input.getEvent("Jump") && (this.onGround || this.currentJumpHeight > 0 && this.currentJumpHeight < this.maxJumpHeight)) {
            this.currentJumpHeight++;
            newVelocity = velocity.add(this.jumpAcceleration.multiply(this.maxJumpHeight - this.currentJumpHeight));
        }
        return newVelocity;
    }

    private Vector2 applyFriction(final Vector2 velocity) {
        if (this.getVelocity().x() != 0.0 && velocity.norm() == 0.0) {
            if (this.friction > 0.0) {
                this.friction -= 0.03125;
            }
        } else {
            this.friction = 1.0;
        }
        return velocity.add(this.getVelocity().multiply(new Vector2(this.friction, 1.0)));
    }

    private Vector2 applyGravity(final Vector2 velocity) {
        return velocity.add(this.gravityAcceleration);
    }

    private void swingSword() {
        if (this.input.getEvent("SwingSword") && !this.swinging) {
            if (!this.swordSwingReset.isRunning()) {
                this.swordSwingStart.start();
            }
            this.swinging = true;
        } else {
            this.swinging = false;
        }
    }

    private void shoot() {
        if (this.input.getEvent("Shoot")) {
            this.world.spawnProjectile();
        }
    }
}
