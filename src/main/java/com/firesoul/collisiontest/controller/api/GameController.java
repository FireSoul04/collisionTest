package com.firesoul.collisiontest.controller.api;

import com.firesoul.collisiontest.controller.api.loader.DrawableLoader;
import com.firesoul.collisiontest.controller.impl.InputController;
import com.firesoul.collisiontest.model.api.gameobjects.Camera;

public interface GameController {

	int getWindowWidth();

	int getWindowHeight();

	int getGameWidth();

	int getGameHeight();

	InputController getInput();

	EventManager getEventManager();

	DrawableLoader getDrawableLoader();

	Camera getCamera();
}
