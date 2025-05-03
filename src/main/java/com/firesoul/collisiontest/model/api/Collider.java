package com.firesoul.collisiontest.model.api;

import java.util.List;
import java.util.Set;

import com.firesoul.collisiontest.model.util.Vector2;

public interface Collider {

    void move(Vector2 position);

    void rotate(double angle);

    List<Vector2> getPoints();

    Set<Collider> getCollidedShapes();

    void attachGameObject(GameObject gameObject);

    GameObject getAttachedGameObject();

    double getOrientation();

    Vector2 getPosition();

    void setPosition(Vector2 position);

    boolean isCollided();

    void addCollided(Collider collidedShape);

    void removeCollided(Collider collidedShape);

    boolean isSolid();

    void setSolid(boolean solid);

    void onCollision(Collider collidedShape, Vector2 collisionDirection, double collisionTime);
}
