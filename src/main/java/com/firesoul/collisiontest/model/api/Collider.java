package com.firesoul.collisiontest.model.api;

import java.util.List;
import java.util.Set;

import com.firesoul.collisiontest.model.util.Vector2;

public interface Collider {

    void move(Vector2 position);

    void rotate(double angle);

    double getOrientation();

    Set<GameObject> getCollidedGameObjects();

    Vector2 getPosition();

    void setPosition(Vector2 position);

    boolean isCollided();

    void addCollided(GameObject collidedGameObject);

    void removeCollided(GameObject collidedGameObject);

    boolean isSolid();

    void setSolid(boolean solid);
}
