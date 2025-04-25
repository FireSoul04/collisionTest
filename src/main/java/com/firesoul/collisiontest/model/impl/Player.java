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

    private final double speed = 0.5;
    private final double rotSpeed = 0.02;

    private final GameObject sword;
    private final GameCollisions world;
    private final InputController input;

    private final Animation swordSwingReset;
    private final Animation swordSwingEnd;
    private final Animation swordSwingStart;
    private boolean swinging = false;

    // Gravity logic
    private final double gforce = 0.25;
    private Vector2 gravityAcceleration = new Vector2(0.0, this.gforce);

    // Jump logic
    private final double jforce = 0.06;
    private Vector2 jumpAcceleration = new Vector2(0.0, -this.jforce);
    public boolean onGround = false;
    private int currentJumpHeight = 0;
    private int maxJumpHeight = 30;
    
    // Movement logic
    private double friction = 1.0;

    public Player(
        final Vector2 position,
        final double orientation,
        final Optional<Collider> collider,
        final Optional<Image> image,
        final GameObject sword,
        final InputController input,
        final GameCollisions world
    ) {
        super(position, orientation, collider, image);

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
        Vector2 movementVelocity = this.getVelocity();
        this.onGround = false;
        this.move(this.getVelocity().multiply(deltaTime));
        this.sword.move(this.getVelocity().multiply(deltaTime));

        // this.setVelocity(this.getVelocity().add(this.gravityAcceleration));
        // System.out.println(this.onGround);
        // System.out.println(this.gravityAcceleration);
    }

    @Override
    public void onCollide(final Collider collidedShape, final Vector2 collisionDirection, final double collisionTime) {
        if (collidedShape.getAttachedGameObject() instanceof Block) {
            if (collisionDirection.equals(new Vector2(0.0, -1.0))) {
                this.onGround = true;
                this.currentJumpHeight = 0;
            }
        }
    }

    public double getSpeed() {
        return this.speed;
    }

    public double getRotationSpeed() {
        return this.rotSpeed;
    }

    public void readInput() {
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

        if (this.input.getEvent("Jump") && (this.onGround || (this.currentJumpHeight > 0 && this.currentJumpHeight < this.maxJumpHeight))) {
            this.currentJumpHeight++;
        }
        if (this.currentJumpHeight > 0 && this.currentJumpHeight < this.maxJumpHeight*3) {
            this.currentJumpHeight++;
            velocity = velocity.add(this.jumpAcceleration.multiply(this.maxJumpHeight - this.currentJumpHeight));
        }

        if (this.input.getEvent("SwingSword") && !this.swinging) {
            this.swingSword();
            this.swinging = true;
        } else {
            this.swinging = false;
        }
        if (this.input.getEvent("Shoot")) {
            this.world.spawnProjectile();
        }

        if (this.getVelocity().x() != 0.0 && velocity.norm() == 0.0) {
            if (this.friction > 0.0) {
                this.friction -= 0.03125;
            }
        } else {
            this.friction = 1.0;
        }
        this.setVelocity(velocity.add(this.getVelocity().multiply(new Vector2(this.friction, 1.0))));
    }

    private void swingSword() {
        if (!this.swordSwingReset.isRunning()) {
            this.swordSwingStart.start();
        }
    }
}
