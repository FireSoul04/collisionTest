package test.model.impl;

import java.awt.Image;
import java.util.Optional;

import test.controller.InputController;
import test.model.api.Collider;
import test.model.api.GameObject;
import test.model.impl.BlockBuilder.Block;
import test.model.util.Vector2;
import test.view.Animation;
import test.view.SwordSwingAnimation;

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
    private Vector2 jumpForce = new Vector2(0.0, -this.jforce);
    private Vector2 gravityForce = Vector2.zero();
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
        Vector2 velocity = this.readInput();
        if (this.input.getEvent("Gravity") && velocity.y() >= 0.0) {
            velocity = velocity.add(this.gravityForce);
            this.gravityForce = this.gravityForce.add(new Vector2(0.0, this.gforce));
        }
        velocity = velocity.multiply(deltaTime);

        Vector2 collisionDirection = Vector2.zero();
        for (final Collider collider : this.getCollider().get().getCollidedShapes().stream().filter(t -> t.getAttachedGameObject() instanceof Block).filter(Collider::isSolid).toList()) {
            Vector2 v = this.getPosition().subtract(collider.getAttachedGameObject().getPosition());
            double x = Math.abs(v.x()) > Math.abs(v.y()) ? Math.signum(v.x()) : 0.0;
            double y = Math.abs(v.y()) > Math.abs(v.x()) ? Math.signum(v.y()) : 0.0;
            collisionDirection = new Vector2(x, y);

            // velocity = this.pushBackX(collider, collisionDirection, velocity);
            // velocity = this.pushBackY(collider, collisionDirection, velocity);

            velocity = velocity.add(v);
        }

        if (this.terrainDetector.getCollider().get().getCollidedShapes().stream().anyMatch(t -> t.getAttachedGameObject() instanceof Block)) {
            this.onGround = true;
        } else {
            this.onGround = false;
        }

        this.move(velocity);
        this.sword.move(velocity);
        this.terrainDetector.move(velocity);
    }

    private Vector2 pushBackX(final Collider collider, final Vector2 collisionDirection, final Vector2 oldVelocity) {
        Vector2 velocity = oldVelocity;
        if (collisionDirection.x() != 0.0 && Math.signum(oldVelocity.x()) == -collisionDirection.x()) {
            double xmin = Double.POSITIVE_INFINITY;
            for (int i = 0; i < this.getCollider().get().getPoints().size(); i++) {
                for (int j = 0; j < collider.getPoints().size(); j++) {
                    var l1 = this.getCollider().get().getPoints().get(i);
                    var l2 = collider.getPoints().get(j);

                    xmin = Math.min(xmin, Math.abs(l1.subtract(l2).x()));
                }
            }
            velocity = new Vector2(xmin*Math.signum(collisionDirection.x()), oldVelocity.y());
        }
        return velocity;
    }

    private Vector2 pushBackY(final Collider collider, final Vector2 collisionDirection, final Vector2 oldVelocity) {
        Vector2 velocity = oldVelocity;
        if (collisionDirection.y() != 0.0 && Math.signum(oldVelocity.y()) == -collisionDirection.y()) {
            double ymin = Double.POSITIVE_INFINITY;
            for (int i = 0; i < this.getCollider().get().getPoints().size(); i++) {
                for (int j = 0; j < collider.getPoints().size(); j++) {
                    var l1 = this.getCollider().get().getPoints().get(i);
                    var l2 = collider.getPoints().get(j);

                    ymin = Math.min(ymin, Math.abs(l1.subtract(l2).y()));
                }
            }
            velocity = new Vector2(oldVelocity.x(), ymin*Math.signum(collisionDirection.y()));
            this.gravityForce = Vector2.zero();
        }
        return velocity;
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
        velocity = velocity.multiply(this.speed);

        if ((this.onGround || (this.currentJumpHeight > 0 && this.currentJumpHeight < this.maxJumpHeight)) && this.input.getEvent("Jump")) {
            this.currentJumpHeight++;
            velocity = velocity.add(this.jumpForce);
            this.jumpForce = this.jumpForce.add(new Vector2(0.0, this.gforce));
        } else {
            this.currentJumpHeight = 0;
            this.jumpForce = new Vector2(0.0, -this.jforce);
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
