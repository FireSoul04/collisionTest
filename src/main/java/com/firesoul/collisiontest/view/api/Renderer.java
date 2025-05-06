package com.firesoul.collisiontest.view.api;

import java.util.List;

import com.firesoul.collisiontest.controller.impl.InputController;
import com.firesoul.collisiontest.model.api.gameobjects.Camera;
import com.firesoul.collisiontest.model.api.GameObject;

public interface Renderer {

    void add(Drawable drawable);

    void update(List<GameObject> gameObjects);

    int getWidth();

    int getHeight();

    int getGameWidth();

    int getGameHeight();

    InputController getInput();

    Camera getCamera();

    DrawableFactory getDrawableFactory();
}
