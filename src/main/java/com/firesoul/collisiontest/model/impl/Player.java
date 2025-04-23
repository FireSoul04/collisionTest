package com.firesoul.collisiontest.model.impl;

import java.awt.Image;
import java.util.List;
import java.util.Optional;

import com.firesoul.collisiontest.controller.impl.InputController;
import com.firesoul.collisiontest.model.api.Collider;
import com.firesoul.collisiontest.model.api.GameObject;
import com.firesoul.collisiontest.model.impl.BlockBuilder.Block;
import com.firesoul.collisiontest.model.util.Vector2;
import com.firesoul.collisiontest.view.impl.Animation;
import com.firesoul.collisiontest.view.impl.SwordSwingAnimation;

public class Player extends GameObjectImpl {

    private final double speed = 3.0;
    private final double rotSpeed = 0.02;

    private final GameObject sword;
    private final GameObject terrainDetector;
    private final GameCollisions world;
    private final InputController input;

    private final Animation swordSwingReset;
    private final Animation swordSwingEnd;
    private final Animation swordSwingStart;
    private boolean swinging = false;

    // Jump logic
    private final double jforce = 7.0;
    private final double gforce = 0.2;
    private Vector2 jumpAcceleration = new Vector2(0.0, -this.jforce);
    private Vector2 gravityAcceleration = Vector2.zero();
    private boolean onGround = true;
    private int currentJumpHeight = 0;
    private int maxJumpHeight = 50;

    public Player(
        final Vector2 position,
        final double orientation,
        final Optional<Collider> collider,
        final Optional<Image> image,
        final GameObject sword,
        final GameObject terrainDetector,
        final InputController input,
        final GameCollisions world
    ) {
        super(position, orientation, collider, image);

        this.sword = sword;
        this.terrainDetector = terrainDetector;
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
        Vector2 velocity = Vector2.zero();

        velocity = velocity.add(this.readInput());
        if (this.input.getEvent("Gravity") && velocity.y() >= 0.0) {
            velocity = velocity.add(this.gravityAcceleration.multiply(deltaTime));
            this.gravityAcceleration = this.gravityAcceleration.add(new Vector2(0.0, this.gforce));
        }
        if (this.terrainDetector.getCollider().get().getCollidedShapes().stream().anyMatch(t -> t.getAttachedGameObject() instanceof Block)) {
            this.onGround = true;
        } else {
            this.onGround = false;
        }

        velocity = velocity.multiply(deltaTime);

        this.move(velocity);
        this.sword.move(velocity);
        this.terrainDetector.move(velocity);
    }

    public double getSpeed() {
        return this.speed;
    }

    public double getRotationSpeed() {
        return this.rotSpeed;
    }

    private Vector2 readInput() {
        Vector2 velocity = Vector2.zero();
        if (this.input.getEvent("MoveLeft")) {
            velocity = velocity.add(new Vector2(-1.0, 0.0));
        }
        if (this.input.getEvent("MoveRight")) {
            velocity = velocity.add(new Vector2(1.0, 0.0));
        }
        if (this.input.getEvent("MoveUp")) {
            velocity = velocity.add(new Vector2(0.0, -1.0));
        }
        if (this.input.getEvent("MoveDown")) {
            velocity = velocity.add(new Vector2(0.0, 1.0));
        }
        velocity = velocity.multiply(this.speed);

        if ((this.onGround || (this.currentJumpHeight > 0 && this.currentJumpHeight < this.maxJumpHeight)) && this.input.getEvent("Jump")) {
            this.currentJumpHeight++;
            velocity = velocity.add(this.jumpAcceleration);
            this.jumpAcceleration = this.jumpAcceleration.add(new Vector2(0.0, this.gforce));
        } else {
            this.currentJumpHeight = 0;
            this.jumpAcceleration = new Vector2(0.0, -this.jforce);
        }

        if (!this.swinging && this.input.getEvent("SwingSword")) {
            this.swingSword();
            this.swinging = true;
        } else {
            this.swinging = false;
        }
        if (this.input.getEvent("Shoot")) {
            this.world.spawnProjectile();
        }
        return velocity;
    }

    private void swingSword() {
        if (!this.swordSwingReset.isRunning()) {
            this.swordSwingStart.start();
        }
    }
}
