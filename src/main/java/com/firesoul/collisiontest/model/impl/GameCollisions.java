package com.firesoul.collisiontest.model.impl;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.firesoul.collisiontest.controller.impl.InputController;
import com.firesoul.collisiontest.model.api.*;
import com.firesoul.collisiontest.model.util.Vector2;
import com.firesoul.collisiontest.view.api.Renderer;

public class GameCollisions implements CollisionTest {

    private static final int TILE_SIZE = 20;

    private final int width = 1600;
    private final int height = 1200;

    private final Renderer renderer;
    private final InputController input;

    private final List<GameObject> gameObjects = new ArrayList<>();
    private final List<GameObject> projectiles = new ArrayList<>();

    private final GameObjectFactory gf;
    private Player player;

    public GameCollisions(final Renderer renderer) {
        this.renderer = renderer;
        this.input = renderer.getInput();
        this.gf = new GameObjectFactoryImpl(this.renderer);
        this.addGameObjects();

        this.input.addEvent("Jump", () -> this.input.isKeyPressed(KeyEvent.VK_SPACE));
        this.input.addEvent("MoveLeft", () -> this.input.isKeyPressed(KeyEvent.VK_A));
        this.input.addEvent("MoveRight", () -> this.input.isKeyPressed(KeyEvent.VK_D));
        this.input.addEvent("MoveUp", () -> this.input.isKeyPressed(KeyEvent.VK_W));
        this.input.addEvent("MoveDown", () -> this.input.isKeyPressed(KeyEvent.VK_S));

        this.input.addEvent("UseWeapon", () -> this.input.isKeyPressedOnce(KeyEvent.VK_E));
        this.input.addEvent("Reload", () -> this.input.isKeyPressedOnce(KeyEvent.VK_R));
        this.input.addEvent("ChangeWeapon", () -> this.input.isKeyPressedOnce(KeyEvent.VK_Q));
    }

    private void addGameObjects() {
        final Vector2 playerPosition = new Vector2(this.renderer.getGameWidth(), this.renderer.getGameHeight()).divide(2.0);
        this.player = this.gf.player(playerPosition, this.input, this);
        this.gameObjects.add(this.gf.ballEnemy(playerPosition.add(Vector2.one().multiply(390))));
        this.gameObjects.add(this.player);

        final WeaponFactory wf = new WeaponFactoryImpl(this.renderer);
        final Weapon sword = wf.sword(this.player);
        final Weapon gun = wf.gun(this.player, this);
        this.gameObjects.add(sword);
        this.gameObjects.add(gun);
        this.player.equip(sword);
        this.player.equip(gun);

        for (int x = 1; x < 50; x++) {
            this.gameObjects.add(this.gf.block(new Vector2(x * GameCollisions.TILE_SIZE, 600)));
        }
    }

    @Override
    public void render() {
        this.renderer.update(this.gameObjects);
    }

    @Override
    public void update(final double deltaTime) {
        this.gameObjects.forEach(t -> t.update(deltaTime));
        this.gameObjects.addAll(this.projectiles);
        this.projectiles.clear();

        this.renderer.getCamera().setPosition(this.player.getPosition().subtract(
            new Vector2(this.renderer.getGameWidth(), this.renderer.getGameHeight())
                .divide(2.0)
        ));

        for (final GameObject g : this.gameObjects) {
            final Vector2 pos = g.getPosition();
            if (
                pos.x() < -this.width ||
                pos.x() > this.height * 2 ||
                pos.y() < -this.width ||
                pos.y() > this.height * 2
            ) {
                g.destroy();
            }
        }

        final Iterator<GameObject> it = this.gameObjects.iterator();
        while (it.hasNext()) {
            final GameObject g = it.next();
            if (!g.isActive()) {
                it.remove();
            }
        }
    }

    @Override
    public List<GameObject> getGameObjects() {
        return this.gameObjects;
    }

    public void spawnProjectile(final Vector2 position, final Vector2 velocity) {
        this.projectiles.add(this.gf.projectile(position, velocity.x()));
    }

    @Override
    public void readInput() {
        this.player.readInput();
    }

    @Override
    public double getWidth() {
        return this.width;
    }

    @Override
    public double getHeight() {
        return this.height;
    }
}
