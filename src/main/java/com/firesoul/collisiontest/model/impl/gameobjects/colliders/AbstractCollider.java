package com.firesoul.collisiontest.model.impl.gameobjects.colliders;

import com.firesoul.collisiontest.model.api.Collider;
import com.firesoul.collisiontest.model.api.GameObject;
import com.firesoul.collisiontest.model.util.Vector2;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class AbstractCollider implements Collider {

    private final Set<GameObject> collidedGameObjects = new HashSet<>();

    private Vector2 position = Vector2.zero();
    private double orientation = 0.0;
    private boolean collided = false;
    private boolean solid = true;

    public AbstractCollider(final Vector2 position) {
        this.position = position;
    }

    @Override
    public void move(final Vector2 position) {
        this.position = this.position.add(position);
    }

    @Override
    public void rotate(final double angle) {
        this.orientation = this.orientation + angle;
    }

    @Override
    public double getOrientation() {
        return this.orientation;
    }

    @Override
    public Set<GameObject> getCollidedGameObjects() {
        return Collections.unmodifiableSet(this.collidedGameObjects);
    }

    @Override
    public Vector2 getPosition() {
        return this.position;
    }

    @Override
    public void setPosition(final Vector2 position) {
        this.position = position;
    }

    @Override
    public boolean isCollided() {
        return this.collided;
    }

    @Override
    public void addCollided(final GameObject collidedGameObject) {
        this.collided = true;
        this.collidedGameObjects.add(collidedGameObject);
    }

    @Override
    public void removeCollided(final GameObject collidedGameObject) {
        this.collidedGameObjects.remove(collidedGameObject);
        if (this.collidedGameObjects.isEmpty()) {
            this.collided = false;
        }
    }

    @Override
    public boolean isSolid() {
        return this.solid;
    }

    @Override
    public void setSolid(final boolean solid) {
        this.solid = solid;
    }
}
