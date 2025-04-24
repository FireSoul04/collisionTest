package com.firesoul.collisiontest.model.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import com.firesoul.collisiontest.model.api.Collider;
import com.firesoul.collisiontest.model.api.GameObject;
import com.firesoul.collisiontest.model.util.Vector2;

public class MeshCollider implements Collider {

    private final Set<Collider> collidedShapes = new HashSet<>();
    private final List<Vector2> points = new CopyOnWriteArrayList<>();
    private Vector2 position = Vector2.zero();
    private GameObject attachedGameObject = new GameObjectImpl(this.position, 0.0, Optional.empty(), Optional.empty());
    private double orientation = 0.0;
    private boolean collided = false;
    private boolean solid = true;

    public MeshCollider(final List<Vector2> points, final double size, final double angle) {
        this.points.addAll(points.stream().map(t -> t.multiply(size)).toList());
        this.rotate(angle);
    }

    @Override
    public void move(final Vector2 position) {
        this.position = this.position.add(position);
    }

    @Override
    public void rotate(final double angle) {
        this.orientation = this.orientation + angle;
        this.points.replaceAll(p -> p.rotate(angle));
    }

    @Override
    public List<Vector2> getPoints() {
        return Collections.unmodifiableList(this.points.stream().map(p -> p.add(this.position)).toList());
    }

    @Override
    public Set<Collider> getCollidedShapes() {
        return Collections.unmodifiableSet(this.collidedShapes);
    }

    @Override
    public void attachGameObject(final GameObject gameObject) {
        this.position = gameObject.getPosition();
        this.attachedGameObject = gameObject;
    }

    @Override
    public GameObject getAttachedGameObject() {
        return this.attachedGameObject;
    }

    @Override
    public Vector2 getPosition() {
        return this.position;
    }

    @Override
    public double getOrientation() {
        return this.orientation;
    }

    @Override
    public boolean isCollided() {
        return this.collided;
    }

    @Override
    public void addCollided(final Collider collidedShape) {
        this.collided = true;
        this.collidedShapes.add(collidedShape);
    }

    @Override
    public void removeCollided(final Collider collidedShape) {
        this.collidedShapes.remove(collidedShape);
        if (this.collidedShapes.isEmpty()) {
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

    @Override
    public void onCollide(final Collider collidedShape, final Vector2 collisionDirection, final double collisionTime) {
        this.attachedGameObject.onCollide(collidedShape, collisionDirection, collisionTime);
    }
}
