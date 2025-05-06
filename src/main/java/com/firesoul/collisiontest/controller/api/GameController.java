package com.firesoul.collisiontest.controller.api;

import com.firesoul.collisiontest.controller.impl.InputController;
import com.firesoul.collisiontest.model.api.gameobjects.Camera;
import com.firesoul.collisiontest.view.api.DrawableFactory;

public interface GameController {

	int getWindowWidth();

	int getWindowHeight();

	int getGameWidth();

	int getGameHeight();

	InputController getInput();

	Camera getCamera();

	DrawableFactory getDrawableFactory();
}
