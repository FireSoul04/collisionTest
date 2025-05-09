package com.firesoul.collisiontest.model.api;

import java.util.Optional;

import com.firesoul.collisiontest.model.api.drawable.Drawable;
import com.firesoul.collisiontest.model.api.physics.Collider;
import com.firesoul.collisiontest.model.util.Vector2;

public interface GameObject {

    void update(double deltaTime);

    void rotate(double angle);

    void move(Vector2 position);

    Vector2 getPosition();

    void setPosition(Vector2 position);

    Vector2 getVelocity();

    void setVelocity(Vector2 velocity);

    double getOrientation();

    Optional<Drawable> getSprite();

    Optional<Collider> getCollider();

    boolean isStatic();

    boolean isDynamic();

    void setSolid(boolean solid);
    
    boolean isActive();

    void onDestroy();

    void destroy();

    default void onCollision(GameObject collidedGameObject, Vector2 collisionDirection, double collisionTime) {}
}
