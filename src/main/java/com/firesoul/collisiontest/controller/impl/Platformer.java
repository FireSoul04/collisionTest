package com.firesoul.collisiontest.controller.impl;

import java.awt.event.KeyEvent;

import com.firesoul.collisiontest.controller.api.EventManager;
import com.firesoul.collisiontest.controller.api.GameLogic;
import com.firesoul.collisiontest.model.api.*;
import com.firesoul.collisiontest.model.impl.LevelImpl;

public class Platformer implements GameLogic {

    private final Level level;

    public Platformer(final GameCore controller) {
        this.level = new LevelImpl(controller);

        final InputController input = controller.getInput();
        final EventManager events = controller.getEventManager();
        events.addEvent("Jump", () -> input.isKeyPressed(KeyEvent.VK_SPACE));
        events.addEvent("MoveLeft", () -> input.isKeyPressed(KeyEvent.VK_A));
        events.addEvent("MoveRight", () -> input.isKeyPressed(KeyEvent.VK_D));
        events.addEvent("MoveUp", () -> input.isKeyPressed(KeyEvent.VK_W));
        events.addEvent("MoveDown", () -> input.isKeyPressed(KeyEvent.VK_S));
        events.addEvent("UseWeapon", () -> input.isKeyPressedOnce(KeyEvent.VK_E));
        events.addEvent("Reload", () -> input.isKeyPressedOnce(KeyEvent.VK_R));
        events.addEvent("ChangeWeapon", () -> input.isKeyPressedOnce(KeyEvent.VK_Q));
    }

    @Override
    public void update(final double deltaTime) {
        this.level.update(deltaTime);
    }

    @Override
    public Level getLevel() {
        return this.level;
    }
}
