package com.firesoul.collisiontest.controller.api;

import com.firesoul.collisiontest.model.api.Level;

public interface GameLogic {

    void update(double deltaTime);

    Level getLevel();
}
