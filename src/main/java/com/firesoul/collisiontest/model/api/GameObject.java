package com.firesoul.collisiontest.model.api;

import java.awt.Image;
import java.util.Optional;

import com.firesoul.collisiontest.model.util.Vector2;

public interface GameObject {

    void update(double deltaTime);

    void rotate(double angle);

    void move(Vector2 position);

    Vector2 getPosition();

    Vector2 getVelocity();

    void setVelocity(Vector2 velocity);

    double getOrientation();

    Optional<Image> getImage();

    Optional<Collider> getCollider();

    boolean isStatic();

    boolean isDynamic();

    void setSolid(boolean solid);
    
    boolean isActive();

    void destroy();

    default void onCollide(Collider collidedShape, Vector2 collisionDirection, double collisionTime) {}
}
