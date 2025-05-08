package com.firesoul.collisiontest.view.api;

import com.firesoul.collisiontest.controller.impl.InputController;

public interface Renderer {

    void add(Renderable renderable);

    void reset();

    void update();

    int getWidth();

    int getHeight();

    int getGameWidth();

    int getGameHeight();

    InputController getInput();
}
