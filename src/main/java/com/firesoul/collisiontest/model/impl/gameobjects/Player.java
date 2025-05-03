package com.firesoul.collisiontest.model.impl.gameobjects;

import java.util.*;

import com.firesoul.collisiontest.controller.impl.InputController;
import com.firesoul.collisiontest.model.api.Collider;
import com.firesoul.collisiontest.model.api.gameobjects.Enemy;
import com.firesoul.collisiontest.model.api.GameObject;
import com.firesoul.collisiontest.model.api.gameobjects.Weapon;
import com.firesoul.collisiontest.model.impl.CollisionAlgorithms;
import com.firesoul.collisiontest.model.impl.gameobjects.weapons.Gun;
import com.firesoul.collisiontest.model.util.GameTimer;
import com.firesoul.collisiontest.model.util.Vector2;
import com.firesoul.collisiontest.view.api.Drawable;

public class Player extends EntityImpl {

    private final double speed = 0.03;
    private final double rotSpeed = 0.015;

    private final InputController input;

    private final Map<String, Drawable> sprites;

    // Attack logic
    private final List<Weapon> weapons = new ArrayList<>();
    private int nextWeapon = 0;
    private Optional<Weapon> equippedWeapon = Optional.empty();
    private final GameTimer weaponCooldown = new GameTimer(() -> {}, 0, 1000);
    // Movement logic
    private double facingDirectionX = 1.0;
    private double friction = 1.0;
    private int currentVelocity = 1;
    private final int maxVelocity = 5;
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
        final InputController input
    ) {
        super(position, orientation, true, collider, Optional.of(sprites.get("idle")), 250, 12);

        this.sprites = sprites;
        this.input = input;
    }

    @Override
    public void update(final double deltaTime) {
        this.move(this.getVelocity().multiply(deltaTime));

        this.sprites.forEach((k, v) -> {
            v.translate(this.getPosition());
            v.mirrorX(this.facingDirectionX);
        });
        this.weapons.forEach(t -> t.setDirectionX(this.facingDirectionX));

        if (this.isInvincible()) {
            this.setSprite(this.sprites.get("damage"));
            this.equippedWeapon.flatMap(GameObject::getSprite).ifPresent(s -> s.setVisible(false));
        } else {
            this.setSprite(this.sprites.get("idle"));
            this.equippedWeapon.flatMap(GameObject::getSprite).ifPresent(s -> s.setVisible(true));
        }
    }

    @Override
    public void onCollision(final Collider collidedShape, final Vector2 collisionDirection, final double collisionTime) {
        final GameObject g = collidedShape.getAttachedGameObject();
        if (g.isStatic() && collisionDirection.equals(Vector2.up())) {
            this.currentJumpHeight = 0;
            this.onGround = true;
        } else if (g instanceof Enemy) {
            this.takeDamage(3);

            final var r1 = CollisionAlgorithms.fitInRect(this.getCollider().get());
            final var r2 = CollisionAlgorithms.fitInRect(collidedShape);
            final double distX = Math.signum((this.getPosition().x() + r1.w()/2.0) - (g.getPosition().x() + r2.w()/2.0));
            this.setVelocity(new Vector2(distX*10, this.getVelocity().y()));
            this.facingDirectionX = -distX;
        }
    }

    @Override
    public void onDestroy() {
        System.out.println("Game over");
        this.weapons.forEach(GameObject::destroy);
        this.input.resetEvents();
    }

    public void equip(final Weapon weapon) {
        this.weapons.add(weapon);
        if (this.equippedWeapon.isEmpty()) {
            this.equippedWeapon = Optional.of(weapon);
            weapon.getSprite().ifPresent(s -> s.setVisible(true));
        }
    }

    public Optional<Weapon> getEquippedWeapon() {
        return this.equippedWeapon;
    }

    public double getSpeed() {
        return this.speed;
    }

    public double getRotationSpeed() {
        return this.rotSpeed;
    }

    public void readInput() {
        Vector2 velocity = Vector2.zero();
        if (!this.isInvincible()) {
            this.equippedWeapon.ifPresent(t -> {
                this.useWeapon(t);
                this.changeWeapon(t);
            });
            velocity = this.inputMove();
            velocity = this.jump(velocity);
        }
        velocity = this.applyFriction(velocity);
        velocity = this.applyGravity(velocity);
        this.onGround = false;
        this.setVelocity(velocity);
    }

    private Vector2 inputMove() {
        Vector2 velocity = Vector2.zero();
        if (this.input.getEvent("MoveLeft")) {
            velocity = velocity.add(Vector2.left().multiply(this.currentVelocity));
            this.facingDirectionX = -1.0;
        }
        if (this.input.getEvent("MoveRight")) {
            velocity = velocity.add(Vector2.right().multiply(this.currentVelocity));
            this.facingDirectionX = 1.0;
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

    private void useWeapon(final Weapon equippedWeapon) {
        if (this.input.getEvent("UseWeapon")) {
            equippedWeapon.attack();
        }
        if (this.input.getEvent("Reload") && equippedWeapon instanceof Gun gun) {
            gun.reload();
        }
    }

    private void changeWeapon(final Weapon equippedWeapon) {
        if (this.input.getEvent("ChangeWeapon") && !this.weaponCooldown.isRunning()) {
            this.weaponCooldown.start();
            this.nextWeapon = (this.nextWeapon + 1) % this.weapons.size();
            this.equippedWeapon = Optional.of(this.weapons.get(this.nextWeapon));
            equippedWeapon.getSprite().ifPresent(s -> s.setVisible(false));
            this.weapons.get(this.nextWeapon).getSprite().ifPresent(s -> s.setVisible(true));
        }
    }
}
