package com.firesoul.collisiontest.controller.api;

import com.firesoul.collisiontest.controller.api.loader.DrawableLoader;
import com.firesoul.collisiontest.controller.impl.ButtonListener;
import com.firesoul.collisiontest.controller.impl.InputListener;
import com.firesoul.collisiontest.model.api.gameobjects.Camera;

public interface GameController {

	int getWindowWidth();

	int getWindowHeight();

	int getGameWidth();

	int getGameHeight();

	InputListener getInputListener();

	ButtonListener getButtonListener();

	EventManager<String> getEventManager();

	DrawableLoader getDrawableLoader();

	Camera getCamera();
}
