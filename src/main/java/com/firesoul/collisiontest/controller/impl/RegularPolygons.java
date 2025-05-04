package com.firesoul.collisiontest.controller.impl;

import java.awt.event.KeyEvent;

import com.firesoul.collisiontest.controller.api.GameLogic;
import com.firesoul.collisiontest.model.api.Level;
import com.firesoul.collisiontest.model.impl.LevelImpl;
import com.firesoul.collisiontest.view.api.Renderer;
import com.firesoul.collisiontest.view.impl.SwingRenderer;

public class RegularPolygons implements GameLogic {

    private final Renderer renderer;
    private final InputController input;

    private final Level world;

    public RegularPolygons(final SwingRenderer renderer) {
        this.renderer = renderer;
        this.input = renderer.getInput();
        this.world = new LevelImpl(this.input);

        this.input.addEvent("MoveUp", () -> this.input.isKeyPressed(KeyEvent.VK_W));
        this.input.addEvent("MoveDown", () -> this.input.isKeyPressed(KeyEvent.VK_S));
        this.input.addEvent("MoveLeft", () -> this.input.isKeyPressed(KeyEvent.VK_A));
        this.input.addEvent("MoveRight", () -> this.input.isKeyPressed(KeyEvent.VK_D));

        this.input.addEvent("RotateLeft", () -> this.input.isKeyPressed(KeyEvent.VK_Z));
        this.input.addEvent("RotateRight", () -> this.input.isKeyPressed(KeyEvent.VK_X));
    }

    @Override
    public void render() {
        this.renderer.update(this.world.getGameObjects());
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
