package com.firesoul.collisiontest.model.api;

import java.util.Optional;

import com.firesoul.collisiontest.model.util.Vector2;
import com.firesoul.collisiontest.view.api.Drawable;

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
