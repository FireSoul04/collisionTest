package com.firesoul.collisiontest.controller.impl;

import java.awt.event.KeyEvent;

import com.firesoul.collisiontest.controller.api.EventManager;
import com.firesoul.collisiontest.controller.api.GameLogic;
import com.firesoul.collisiontest.controller.api.loader.LevelLoader;
import com.firesoul.collisiontest.controller.impl.loader.TileBasedLevelLoader;
import com.firesoul.collisiontest.model.api.*;

public class Platformer implements GameLogic {

    private final EventManager events;
    private final LevelLoader ld;
    private Level level;
    private State state = State.MENU;

    public Platformer(final GameCore controller) {
        this.ld = new TileBasedLevelLoader(controller);
        this.level = this.ld.readLevel();

        final InputController input = controller.getInput();
        events = controller.getEventManager();
        events.addEvent("Jump", () -> input.isKeyPressed(KeyEvent.VK_SPACE));
        events.addEvent("MoveLeft", () -> input.isKeyPressed(KeyEvent.VK_A));
        events.addEvent("MoveRight", () -> input.isKeyPressed(KeyEvent.VK_D));
        events.addEvent("UseWeapon", () -> input.isKeyPressedOnce(KeyEvent.VK_E));
        events.addEvent("Reload", () -> input.isKeyPressedOnce(KeyEvent.VK_R));
        events.addEvent("ChangeWeapon", () -> input.isKeyPressedOnce(KeyEvent.VK_Q));
        events.addEvent("Start", () -> input.isKeyPressedOnce(KeyEvent.VK_ENTER));
    }

    @Override
    public void update(final double deltaTime) {
        if (this.isRunning()) {
            this.level.update(deltaTime);
            if (events.getEvent("GameOver")) {
                this.state = State.OVER;
            }
        } else if (this.isOnMenu()) {
            if (events.getEvent("Start")) {
                this.state = State.RUNNING;
            }
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
