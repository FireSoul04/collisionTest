package com.firesoul.collisiontest.controller.impl;

import java.awt.event.KeyEvent;

import com.firesoul.collisiontest.controller.api.GameLogic;
import com.firesoul.collisiontest.model.api.*;
import com.firesoul.collisiontest.model.impl.LevelImpl;

public class Platformer implements GameLogic {

    private final Level level;

    public Platformer(final GameCore controller) {
        this.level = new LevelImpl(controller);

        final InputController input = controller.getInput();
        input.addEvent("Jump", () -> input.isKeyPressed(KeyEvent.VK_SPACE));
        input.addEvent("MoveLeft", () -> input.isKeyPressed(KeyEvent.VK_A));
        input.addEvent("MoveRight", () -> input.isKeyPressed(KeyEvent.VK_D));
        input.addEvent("MoveUp", () -> input.isKeyPressed(KeyEvent.VK_W));
        input.addEvent("MoveDown", () -> input.isKeyPressed(KeyEvent.VK_S));

        input.addEvent("UseWeapon", () -> input.isKeyPressedOnce(KeyEvent.VK_E));
        input.addEvent("Reload", () -> input.isKeyPressedOnce(KeyEvent.VK_R));
        input.addEvent("ChangeWeapon", () -> input.isKeyPressedOnce(KeyEvent.VK_Q));
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
