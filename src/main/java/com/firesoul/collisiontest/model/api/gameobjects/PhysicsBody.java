package com.firesoul.collisiontest.model.api.gameobjects;

import com.firesoul.collisiontest.model.util.Vector2;

public interface PhysicsBody {

    void update();

    Vector2 getVelocity();

    void setVelocity(Vector2 velocity);

    void move(Vector2 direction);

    void applyVelocity(Vector2 velocity);
}
