package com.firesoul.collisiontest.controller.api;

import com.firesoul.collisiontest.model.api.Level;

public interface GameLogic {

    enum State {
        MENU,
        PAUSE,
        RUNNING,
        OVER
    }

    void update(double deltaTime);

    boolean isRunning();

    boolean isPaused();

    boolean isOnMenu();

    boolean isOver();

    void setState(State state);

    Level getLevel();
}
