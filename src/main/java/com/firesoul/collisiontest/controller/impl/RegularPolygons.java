package com.firesoul.collisiontest.controller.impl;

import java.awt.event.KeyEvent;

import com.firesoul.collisiontest.controller.api.EventManager;
import com.firesoul.collisiontest.controller.api.GameLogic;
import com.firesoul.collisiontest.model.api.Level;
import com.firesoul.collisiontest.model.impl.LevelImpl;

public class RegularPolygons implements GameLogic {

    private final Level world;

    public RegularPolygons(final GameCore controller) {
        this.world = new LevelImpl(controller);

        final InputController input = controller.getInput();
        final EventManager events = new EventManagerImpl();
        events.addEvent("MoveUp", () -> input.isKeyPressed(KeyEvent.VK_W));
        events.addEvent("MoveDown", () -> input.isKeyPressed(KeyEvent.VK_S));
        events.addEvent("MoveLeft", () -> input.isKeyPressed(KeyEvent.VK_A));
        events.addEvent("MoveRight", () -> input.isKeyPressed(KeyEvent.VK_D));
        events.addEvent("RotateLeft", () -> input.isKeyPressed(KeyEvent.VK_Z));
        events.addEvent("RotateRight", () -> input.isKeyPressed(KeyEvent.VK_X));
    }

    @Override
    public void update(final double deltaTime) {
        this.world.update(deltaTime);
    }

    @Override
    public Level getLevel() {
        return this.world;
    }
}
