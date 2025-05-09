package com.firesoul.collisiontest.model.impl.gameobjects.weapons;

import com.firesoul.collisiontest.model.api.physics.Collider;
import com.firesoul.collisiontest.model.api.GameObject;
import com.firesoul.collisiontest.model.api.Level;
import com.firesoul.collisiontest.model.api.gameobjects.Weapon;
import com.firesoul.collisiontest.model.impl.gameobjects.GameObjectImpl;
import com.firesoul.collisiontest.model.util.Vector2;
import com.firesoul.collisiontest.model.api.drawable.Drawable;

import java.util.Optional;

public class WeaponImpl extends GameObjectImpl implements Weapon {

    private final GameObject holder;
    private final Vector2 offset;
    private final Vector2 spriteOffset;

    private double directionX = 1.0;

    public WeaponImpl(
            final GameObject holder,
            final Vector2 offset,
            final Vector2 spriteOffset,
            final Level world,
            final Optional<Collider> collider,
            final Optional<Drawable> sprite
    ) {
        super(holder.getPosition().add(offset), true, world, collider, sprite);
        this.holder = holder;
        this.offset = offset;
        this.spriteOffset = spriteOffset;
        this.getSprite().ifPresent(s -> s.setVisible(false));
    }

    @Override
    public void update(final double deltaTime) {
        final Vector2 position = new Vector2(
            this.spriteOffset.x() * this.directionX,
            this.spriteOffset.y()
        );
        this.setPosition(this.holder.getPosition().add(this.offset));
        this.getSprite().ifPresent(t -> {
            t.translate(this.getHolder().getPosition().add(position));
            t.mirrorX(this.directionX);
        });
    }

    @Override
    public void attack() {

    }

    @Override
    public GameObject getHolder() {
        return this.holder;
    }

    @Override
    public void setDirectionX(double directionX) {
        final double direction = Math.signum(directionX);
        this.directionX = direction == 0.0 ? this.directionX : direction;
    }

    protected Vector2 getOffset() {
        return this.offset;
    }

    protected Vector2 getSpriteOffset() {
        return this.spriteOffset;
    }

    protected double getDirectionX() {
        return this.directionX;
    }
}
