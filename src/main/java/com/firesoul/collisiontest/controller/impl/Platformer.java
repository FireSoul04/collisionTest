package com.firesoul.collisiontest.controller.impl;

import java.awt.event.KeyEvent;

import com.firesoul.collisiontest.controller.api.EventManager;
import com.firesoul.collisiontest.controller.api.GameLogic;
import com.firesoul.collisiontest.controller.api.loader.LevelLoader;
import com.firesoul.collisiontest.controller.impl.loader.TileBasedLevelLoader;
import com.firesoul.collisiontest.model.api.*;

public class Platformer implements GameLogic {

	private final LevelLoader ld;
    private Level level;
    private State state = State.MENU;

    final EventManager<String> eventManager;
    public Platformer(final GameCore controller) {
        this.ld = new TileBasedLevelLoader(controller);
        this.level = this.ld.readLevel();

        final InputListener inputListener = controller.getInputListener();
        final ButtonListener buttonListener = controller.getButtonListener();
	    eventManager = controller.getEventManager();
        eventManager.addEvent("Jump", () -> inputListener.isKeyPressed(KeyEvent.VK_SPACE));
        eventManager.addEvent("MoveLeft", () -> inputListener.isKeyPressed(KeyEvent.VK_A));
        eventManager.addEvent("MoveRight", () -> inputListener.isKeyPressed(KeyEvent.VK_D));
        eventManager.addEvent("UseWeapon", () -> inputListener.isKeyPressedOnce(KeyEvent.VK_E));
        eventManager.addEvent("Reload", () -> inputListener.isKeyPressedOnce(KeyEvent.VK_R));
        eventManager.addEvent("ChangeWeapon", () -> inputListener.isKeyPressedOnce(KeyEvent.VK_Q));
        eventManager.addEvent("Start", () -> buttonListener.isButtonClicked("Start"));
        eventManager.addEvent("Menu", this::isOnMenu);
        eventManager.addEvent("Exit", () -> buttonListener.isButtonClicked("Exit"));

        eventManager.attachActionOnEvent("Start", () -> this.state = State.RUNNING);
        eventManager.attachActionOnEvent("Menu", () -> this.state = State.MENU);
        eventManager.attachActionOnEvent("GameOver", () -> this.state = State.OVER);
        eventManager.attachActionOnEvent("Exit", () -> System.exit(0));
    }

    @Override
    public void update(final double deltaTime) {
        if (this.isRunning()) {
            this.level.update(deltaTime);
        } else if (this.isOver()) {
            this.level = this.ld.readLevel();
            this.state = State.MENU;
        }
    }

    @Override
    public boolean isRunning() {
        return this.state == State.RUNNING;
    }

    @Override
    public boolean isOnMenu() {
        return this.state == State.MENU;
    }

    @Override
    public boolean isOver() {
        return this.state == State.OVER;
    }

    @Override
    public void setState(final State state) {
        this.state = state;
    }

    @Override
    public Level getLevel() {
        return this.level;
    }
}
