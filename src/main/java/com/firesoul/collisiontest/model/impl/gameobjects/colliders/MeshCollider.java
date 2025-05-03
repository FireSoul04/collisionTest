package com.firesoul.collisiontest.model.impl.gameobjects.colliders;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import com.firesoul.collisiontest.model.api.Collider;
import com.firesoul.collisiontest.model.util.Vector2;

public class MeshCollider implements Collider {

    private final Set<Collider> collidedShapes = new HashSet<>();
    private final List<Vector2> points = new CopyOnWriteArrayList<>();
    private Vector2 position = Vector2.zero();
    private double orientation = 0.0;
    private boolean collided = false;
    private boolean solid = true;

    public MeshCollider(final List<Vector2> points, final double size, final double angle) {
        this.points.addAll(points.stream().map(t -> t.multiply(size)).toList());
        this.rotate(angle);
    }

    public MeshCollider(final List<Vector2> points, final double size) {
        this(points, size, 0.0);
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
        return this.points.stream().map(p -> p.add(this.position)).toList();
    }

    @Override
    public Set<Collider> getCollidedShapes() {
        return Collections.unmodifiableSet(this.collidedShapes);
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
}
