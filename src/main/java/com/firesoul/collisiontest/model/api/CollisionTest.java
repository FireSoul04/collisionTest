package com.firesoul.collisiontest.model.api;

import java.util.List;

public interface CollisionTest {

    void render();

    void update(double deltaTime);

    void readInput();

    List<GameObject> getGameObjects();

    double getWidth();

    double getHeight();
}
