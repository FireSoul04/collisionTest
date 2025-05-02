package com.firesoul.collisiontest.model.impl;

import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.Optional;

import com.firesoul.collisiontest.controller.impl.InputController;
import com.firesoul.collisiontest.model.api.Collider;
import com.firesoul.collisiontest.model.api.Enemy;
import com.firesoul.collisiontest.model.api.GameObject;
import com.firesoul.collisiontest.model.api.Weapon;
import com.firesoul.collisiontest.model.util.GameTimer;
import com.firesoul.collisiontest.model.util.Vector2;
import com.firesoul.collisiontest.view.api.Drawable;

public class Player extends EntityImpl {

    private final double speed = 0.05;
    private final double rotSpeed = 0.015;

    private Weapon weapon;
    private final GameCollisions world;
    private final InputController input;

    private final Map<String, Drawable> sprites;

    // Attack logic
    private final GameTimer swingCoolDown = new GameTimer(() -> {}, 0, 300);
    private final GameTimer shootCooldown = new GameTimer(() -> {}, 0, 500);
    // Movement logic
    private double friction = 1.0;
    private int currentVelocity = 1;
    private final int maxVelocity = 10;
    // Gravity logic
    private final Vector2 gravityAcceleration = new Vector2(0.0, 0.25);
    // Jump logic
    private final Vector2 jumpAcceleration = new Vector2(0.0, -0.06);
    private boolean onGround = false;
    private final int maxJumpHeight = 20;
    private int currentJumpHeight = 0;

    public Player(
        final Vector2 position,
        final double orientation,
        final Optional<Collider> collider,
        final Map<String, Drawable> sprites,
        final InputController input,
        final GameCollisions world
    ) {
        super(position, orientation, true, collider, Optional.of(sprites.get("idle")), 250, 12);

        this.sprites = sprites;
        this.input = input;
        this.world = world;

        this.input.addEvent("Jump", () -> this.input.isKeyPressed(KeyEvent.VK_SPACE));
        this.input.addEvent("MoveLeft", () -> this.input.isKeyPressed(KeyEvent.VK_A));
        this.input.addEvent("MoveRight", () -> this.input.isKeyPressed(KeyEvent.VK_D));
        this.input.addEvent("MoveUp", () -> this.input.isKeyPressed(KeyEvent.VK_W));
        this.input.addEvent("MoveDown", () -> this.input.isKeyPressed(KeyEvent.VK_S));

        this.input.addEvent("SwingSword", () -> this.input.isKeyPressedOnce(KeyEvent.VK_E));
        this.input.addEvent("Shoot", () -> this.input.isKeyPressedOnce(KeyEvent.VK_Q));
    }

    @Override
    public void update(final double deltaTime) {
        this.move(this.getVelocity().multiply(deltaTime));

        this.sprites.forEach((k, v) -> v.translate(this.getPosition()));

        if (this.isInvincible()) {
            this.setSprite(this.sprites.get("damage"));
        } else {
            this.setSprite(this.sprites.get("idle"));
        }
    }

    @Override
    public void onCollide(final Collider collidedShape, final Vector2 collisionDirection, final double collisionTime) {
        final GameObject g = collidedShape.getAttachedGameObject();
        if (g.isStatic() && collisionDirection.equals(Vector2.up())) {
            this.currentJumpHeight = 0;
            this.onGround = true;
        } else if (g instanceof Enemy) {
            this.takeDamage(3);

            final var r1 = CollisionAlgorithms.fitInRect(this.getCollider().get());
            final var r2 = CollisionAlgorithms.fitInRect(collidedShape);
            final double distX = Math.signum((this.getPosition().x() + r1.w()/2.0) - (g.getPosition().x() + r2.w()/2.0));
            System.out.println(distX);
            this.setVelocity(new Vector2(distX*10, this.getVelocity().y()));
        }
    }

    @Override
    public void onDestroy() {
        System.out.println("Game over");
        this.weapon.destroy();
        this.input.resetEvents();
    }

    public void equip(final Weapon weapon) {
        this.weapon = weapon;
    }

    public Weapon getWeapon() {
        return this.weapon;
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

        if (this.isInvincible()) {
            velocity = Vector2.zero();
        }

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
        if (this.input.getEvent("SwingSword") && !this.swingCoolDown.isRunning()) {
            this.swingCoolDown.start();
            this.weapon.attack();
        }
    }

    private void shoot() {
        if (this.input.getEvent("Shoot") && !this.shootCooldown.isRunning()) {
            this.shootCooldown.start();
            this.world.spawnProjectile();
        }
    }
}
