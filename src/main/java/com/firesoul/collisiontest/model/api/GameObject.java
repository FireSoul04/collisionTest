package com.firesoul.collisiontest.model.api;

import java.awt.Image;
import java.util.Optional;

import com.firesoul.collisiontest.model.util.Vector2;

public interface GameObject {

    void update(double deltaTime);

    void rotate(double angle);

    void move(Vector2 position);

    void setSolid(boolean solid);

    Vector2 getPosition();

    Vector2 getVelocity();

    void setVelocity(Vector2 velocity);

    double getOrientation();

    Optional<Image> getImage();

    Optional<Collider> getCollider();

    default void onCollide(Collider collidedShape, Vector2 collisionDirection) {}
}
