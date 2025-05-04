package com.firesoul.collisiontest.model.impl.gameobjects;

import java.util.*;

import com.firesoul.collisiontest.controller.impl.InputController;
import com.firesoul.collisiontest.model.api.Collider;
import com.firesoul.collisiontest.model.api.Level;
import com.firesoul.collisiontest.model.api.gameobjects.Enemy;
import com.firesoul.collisiontest.model.api.GameObject;
import com.firesoul.collisiontest.model.api.physics.PhysicsBody;
import com.firesoul.collisiontest.model.api.gameobjects.Weapon;
import com.firesoul.collisiontest.model.impl.CollisionAlgorithms;
import com.firesoul.collisiontest.model.impl.gameobjects.colliders.BoxCollider;
import com.firesoul.collisiontest.model.impl.gameobjects.weapons.Gun;
import com.firesoul.collisiontest.model.impl.physics.EnhancedPhysicsBody;
import com.firesoul.collisiontest.model.util.GameTimer;
import com.firesoul.collisiontest.model.util.Vector2;
import com.firesoul.collisiontest.view.api.Drawable;

public class Player extends EntityImpl {

    private final double speed = 0.10;

    private final InputController input;

    private final Map<String, Drawable> sprites;

    private final PhysicsBody body = new EnhancedPhysicsBody(
            new Vector2(0.0, 0.25),
            new Vector2(0.012, 0.0),
            new Vector2(1.0, 0.0)
    );

    // Attack logic
    private final List<Weapon> weapons = new ArrayList<>();
    private int nextWeapon = 0;
    private Optional<Weapon> equippedWeapon = Optional.empty();
    private final GameTimer weaponCooldown = new GameTimer(1000);
    // Movement logic
    private double facingDirectionX = 1.0;
    // Jump logic
    private final Vector2 jumpAcceleration = new Vector2(0.0, -0.125);
    private boolean onGround = false;
    private final int maxJumpHeight = 40;
    private int currentJumpHeight = 0;

    public Player(
        final Vector2 position,
        final Level world,
        final Optional<Collider> collider,
        final Map<String, Drawable> sprites,
        final InputController input
    ) {
        super(position, true, world, collider, Optional.of(sprites.get("idle")), 250, 12);

        this.sprites = sprites;
        this.input = input;
    }

    @Override
    public void update(final double deltaTime) {
        this.move(this.getVelocity().multiply(deltaTime));
        this.body.update();
        this.setVelocity(this.body.getVelocity());

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
    public void onCollision(final GameObject gameObject, final Vector2 collisionDirection, final double collisionTime) {
        if (gameObject.isStatic() && collisionDirection.equals(Vector2.up())) {
            this.currentJumpHeight = 0;
            this.onGround = true;
        } else if (gameObject instanceof Enemy) {
            this.takeDamage(3);

            final BoxCollider r1 = CollisionAlgorithms.getBoxCollider(this.getCollider().orElseThrow());
            final BoxCollider r2 = CollisionAlgorithms.getBoxCollider(gameObject.getCollider().orElseThrow());
            final double distX = Math.signum(
                    (this.getPosition().x() + r1.getWidth()/2.0) - (gameObject.getPosition().x() + r2.getWidth()/2.0)
            );
            this.body.setVelocity(new Vector2(distX*collisionTime, 0.0));
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

    public void readInput() {
        if (!this.isInvincible()) {
            this.equippedWeapon.ifPresent(t -> {
                this.useWeapon(t);
                this.changeWeapon(t);
            });
            this.inputMove();
            this.jump();
        }
        this.onGround = false;
    }

    private void inputMove() {
        Vector2 velocity = Vector2.zero();
        if (this.input.getEvent("MoveLeft")) {
            velocity = velocity.add(Vector2.left());
            this.facingDirectionX = -1.0;
        }
        if (this.input.getEvent("MoveRight")) {
            velocity = velocity.add(Vector2.right());
            this.facingDirectionX = 1.0;
        }
        this.body.move(velocity.multiply(this.speed));
    }

    private void jump() {
        if (this.input.getEvent("Jump") && (this.onGround || this.currentJumpHeight > 0 && this.currentJumpHeight < this.maxJumpHeight)) {
            this.currentJumpHeight++;
            this.body.setVelocity(new Vector2(
                this.body.getVelocity().x(),
                this.jumpAcceleration.multiply(this.maxJumpHeight - this.currentJumpHeight).y())
            );
        }
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
