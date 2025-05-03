package com.firesoul.collisiontest.model.impl.gameobjects.colliders;

import com.firesoul.collisiontest.model.api.Collider;
import com.firesoul.collisiontest.model.util.Vector2;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BoxCollider implements Collider {

    private final Set<Collider> collidedShapes = new HashSet<>();
    private final double width;
    private final double height;

    private Vector2 position = Vector2.zero();
    private double orientation = 0.0;
    private boolean collided = false;
    private boolean solid = true;

    public BoxCollider(final Vector2 position, final double width, final double height, final double angle) {
        this.position = position;
        this.width = width;
        this.height = height;
        this.rotate(angle);
    }

    @Override
    public void move(Vector2 position) {

    }

    @Override
    public void rotate(double angle) {

    }

    @Override
    public List<Vector2> getPoints() {
        return List.of();
    }

    @Override
    public Set<Collider> getCollidedShapes() {
        return Set.of();
    }

    @Override
    public double getOrientation() {
        return 0;
    }

    @Override
    public Vector2 getPosition() {
        return null;
    }

    @Override
    public void setPosition(Vector2 position) {

    }

    @Override
    public boolean isCollided() {
        return false;
    }

    @Override
    public void addCollided(Collider collidedShape) {

    }

    @Override
    public void removeCollided(Collider collidedShape) {

    }

    @Override
    public boolean isSolid() {
        return false;
    }

    @Override
    public void setSolid(boolean solid) {

    }
}
