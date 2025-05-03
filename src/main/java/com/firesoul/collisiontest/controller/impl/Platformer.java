package com.firesoul.collisiontest.controller.impl;

import java.awt.event.KeyEvent;
import java.util.Optional;

import com.firesoul.collisiontest.controller.api.GameLogic;
import com.firesoul.collisiontest.model.api.*;
import com.firesoul.collisiontest.model.impl.LevelImpl;
import com.firesoul.collisiontest.model.util.Vector2;
import com.firesoul.collisiontest.view.api.Drawable;
import com.firesoul.collisiontest.view.api.Renderer;

public class Platformer implements GameLogic {

    private final Renderer renderer;
    private final InputController input;
    private final Level level;

    public Platformer(final Renderer renderer) {
        this.renderer = renderer;
        this.input = renderer.getInput();
        this.level = new LevelImpl(this.input);
        this.level.getGameObjects()
                .stream()
                .map(GameObject::getSprite)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(renderer::add);

        this.renderer.getCamera().setBoundsX(this.level.getHeight());
        this.renderer.getCamera().setBoundsY(this.level.getHeight());

        this.input.addEvent("Jump", () -> this.input.isKeyPressed(KeyEvent.VK_SPACE));
        this.input.addEvent("MoveLeft", () -> this.input.isKeyPressed(KeyEvent.VK_A));
        this.input.addEvent("MoveRight", () -> this.input.isKeyPressed(KeyEvent.VK_D));
        this.input.addEvent("MoveUp", () -> this.input.isKeyPressed(KeyEvent.VK_W));
        this.input.addEvent("MoveDown", () -> this.input.isKeyPressed(KeyEvent.VK_S));

        this.input.addEvent("UseWeapon", () -> this.input.isKeyPressedOnce(KeyEvent.VK_E));
        this.input.addEvent("Reload", () -> this.input.isKeyPressedOnce(KeyEvent.VK_R));
        this.input.addEvent("ChangeWeapon", () -> this.input.isKeyPressedOnce(KeyEvent.VK_Q));
    }

    @Override
    public void render() {
        this.renderer.update(this.level.getGameObjects());
    }

    @Override
    public void update(final double deltaTime) {
        this.level.update(deltaTime);
        this.renderer.getCamera().setPosition(this.level.getPlayerPosition().subtract(
            new Vector2(this.renderer.getGameWidth(), this.renderer.getGameHeight()).divide(2.0)
        ));
    }
}
