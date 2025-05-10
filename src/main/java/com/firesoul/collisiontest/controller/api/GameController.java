package com.firesoul.collisiontest.controller.api;

import com.firesoul.collisiontest.controller.api.loader.DrawableLoader;
import com.firesoul.collisiontest.controller.impl.InputListener;
import com.firesoul.collisiontest.model.api.gameobjects.Camera;

public interface GameController {

	int getWindowWidth();

	int getWindowHeight();

	int getGameWidth();

	int getGameHeight();

	InputListener getInput();

	EventManager getEventManager();

	DrawableLoader getDrawableLoader();

	Camera getCamera();
}
