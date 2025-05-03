package com.firesoul.collisiontest.model.api;

import com.firesoul.collisiontest.model.util.Vector2;

import java.util.List;

public interface Level {

    void update(double deltaTime);

    List<GameObject> getGameObjects();

    double getWidth();

    double getHeight();

    Vector2 getPlayerPosition();

    void spawnProjectile(Vector2 position, Vector2 velocity);
}
