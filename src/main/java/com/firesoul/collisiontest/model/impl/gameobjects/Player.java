package com.firesoul.collisiontest.model.impl.gameobjects;

import java.util.*;

import com.firesoul.collisiontest.controller.api.EventManager;
import com.firesoul.collisiontest.model.api.physics.Collider;
import com.firesoul.collisiontest.model.api.Level;
import com.firesoul.collisiontest.model.api.gameobjects.Enemy;
import com.firesoul.collisiontest.model.api.GameObject;
import com.firesoul.collisiontest.model.api.physics.RigidBody;
import com.firesoul.collisiontest.model.api.gameobjects.Weapon;
import com.firesoul.collisiontest.model.impl.CollisionAlgorithms;
import com.firesoul.collisiontest.model.impl.gameobjects.bars.GameBar;
import com.firesoul.collisiontest.model.impl.physics.EnhancedRigidBody;
import com.firesoul.collisiontest.model.impl.physics.colliders.BoxCollider;
import com.firesoul.collisiontest.model.impl.gameobjects.weapons.Gun;
import com.firesoul.collisiontest.model.util.GameTimer;
import com.firesoul.collisiontest.model.util.Vector2;
import com.firesoul.collisiontest.model.api.drawable.Drawable;

public class Player extends EntityImpl {

    public final RigidBody body = new EnhancedRigidBody(new Vector2(5.0, 0.0));
    private final EventManager<String> events;
    private final Map<String, Drawable> sprites;

    private final GameBar lifeBar;

    // Attack logic
    private final List<Weapon> weapons = new ArrayList<>();
    private int nextWeapon = 0;
    private Optional<Weapon> equippedWeapon = Optional.empty();
    private final GameTimer weaponCooldown = new GameTimer(1000, this.getWorld());
    // Movement logic
    private final double speed = 0.2;
    private double facingDirectionX = 1.0;
    // Jump logic
    private final Vector2 jumpAcceleration = new Vector2(0.0, -0.125);
    private boolean onGround = false;
    private final int maxJumpHeight = 11;
    private int currentJumpHeight = 0;

    public Player(
        final Vector2 position,
        final Level world,
        final Optional<Collider> collider,
        final Map<String, Drawable> sprites,
        final EventManager<String> events,
        final GameBar lifeBar
    ) {
        super(position, true, world, collider, Optional.of(sprites.get("idle")), 250, 12);

        this.lifeBar = lifeBar;
        this.sprites = sprites;
        this.events = events;

        this.events.addEvent("Invincible", this::isInvincible);
        this.events.addEvent("Vulnerable", () -> !this.isInvincible());

        this.events.attachActionOnEvent("Invincible", this::whileInvincible);
        this.events.attachActionOnEvent("Vulnerable", this::whileVulnerable);
        this.events.attachActionOnEvent("ChangeWeapon", this::changeWeapon);
        this.events.attachActionOnEvent("UseWeapon", this::useWeapon);
        this.events.attachActionOnEvent("Reload", this::reload);
    }

    @Override
    public Vector2 getVelocity() {
        return this.body.getVelocity();
    }

    @Override
    public void setVelocity(final Vector2 velocity) {
        this.body.setVelocity(velocity);
    }

    @Override
    public void update(final double deltaTime) {
        this.move(this.getVelocity().multiply(deltaTime));
        this.body.update();

        this.sprites.values().forEach(d -> {
            d.translate(this.getPosition());
            d.mirrorX(this.facingDirectionX);
        });
        this.weapons.forEach(t -> t.setDirectionX(this.facingDirectionX));
    }

    @Override
    public void onCollision(final GameObject gameObject, final Vector2 collisionDirection, final double collisionTime) {
        if (gameObject.isStatic() && collisionDirection.equals(Vector2.up())) {
            this.currentJumpHeight = 0;
            this.onGround = true;
        } else if (gameObject instanceof Enemy && !this.isInvincible()) {
            this.takeDamage(3);

            final BoxCollider r1 = CollisionAlgorithms.getBoxCollider(this.getCollider().orElseThrow());
            final BoxCollider r2 = CollisionAlgorithms.getBoxCollider(gameObject.getCollider().orElseThrow());
            final double distX = Math.signum(
                (this.getPosition().x() + r1.getWidth()/2.0) - (gameObject.getPosition().x() + r2.getWidth()/2.0)
            );
            this.body.applyForce(collisionDirection.multiply(collisionTime * 3).invert());
            this.facingDirectionX = -distX;
        }
    }

    @Override
    public void takeDamage(final int amount) {
        super.takeDamage(amount);
        this.lifeBar.setCurrentValue(this.getLife());
    }

    @Override
    public void destroy() {
        super.destroy();
        this.weapons.forEach(GameObject::destroy);
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
            this.inputMove();
            this.jump();
        }
    }

    private void inputMove() {
        Vector2 velocity = Vector2.zero();
        if (this.events.getEvent("MoveLeft")) {
            velocity = velocity.add(Vector2.left());
            this.facingDirectionX = -1.0;
        }
        if (this.events.getEvent("MoveRight")) {
            velocity = velocity.add(Vector2.right());
            this.facingDirectionX = 1.0;
        }
        this.body.move(velocity.multiply(this.speed));
    }

    private void jump() {
        if (this.events.getEvent("Jump") && (this.onGround || this.currentJumpHeight > 0 && this.currentJumpHeight < this.maxJumpHeight)) {
            this.currentJumpHeight++;
            this.onGround = false;
            this.body.applyForce(this.jumpAcceleration.multiply(this.maxJumpHeight - this.currentJumpHeight));
        }
        if (!this.events.getEvent("Jump")) {
            this.currentJumpHeight = 0;
        }
    }

    private void whileInvincible() {
        this.setSprite(this.sprites.get("damage"));
        this.equippedWeapon.flatMap(GameObject::getSprite).ifPresent(s -> s.setVisible(false));
    }

    private void whileVulnerable() {
        this.setSprite(this.sprites.get("idle"));
        this.equippedWeapon.flatMap(GameObject::getSprite).ifPresent(s -> s.setVisible(true));
    }

    private void changeWeapon() {
        if (!this.weaponCooldown.isRunning() && this.equippedWeapon.isPresent()) {
            if (equippedWeapon.get() instanceof Gun gun) {
                gun.resetCooldown();
            }

            this.weaponCooldown.start();
            this.nextWeapon = (this.nextWeapon + 1) % this.weapons.size();
            this.equippedWeapon.get().getSprite().ifPresent(s -> s.setVisible(false));
            this.equippedWeapon = Optional.of(this.weapons.get(this.nextWeapon));
            this.equippedWeapon.get().getSprite().ifPresent(s -> s.setVisible(true));
        }
    }

    private void useWeapon() {
        if (!this.isInvincible()) {
            this.equippedWeapon.ifPresent(Weapon::attack);
        }
    }

    private void reload() {
        if (!this.isInvincible() && equippedWeapon.isPresent() && equippedWeapon.get() instanceof Gun gun) {
            gun.reload();
        }
    }
}
