package com.firesoul.collisiontest.model.impl;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import com.firesoul.collisiontest.controller.impl.Controller;
import com.firesoul.collisiontest.controller.impl.InputController;
import com.firesoul.collisiontest.model.api.CollisionTest;
import com.firesoul.collisiontest.model.api.GameObject;
import com.firesoul.collisiontest.model.api.GameObjectBuilder;
import com.firesoul.collisiontest.model.util.Vector2;
import com.firesoul.collisiontest.view.impl.Renderer;

public class RegularPolygons implements CollisionTest {

    private final Renderer w;
    private final InputController input;
    private final double speed = 1.0;
    private final double rotSpeed = 0.1;

    private final List<GameObject> gameObjects = new ArrayList<>();
    private final GameObject player;

    public RegularPolygons(final Renderer w) {
        this.w = w;
        this.input = w.getInput();

        GameObjectBuilder playerBuilder = new GameObjectBuilderImpl(new Vector2(w.getWidth(), w.getHeight()).divide(4.0), true);
        playerBuilder = playerBuilder.collider(new MeshCollider(Controller.regularPolygon(5), 50.0, 0.0));
        this.player = playerBuilder.build();
        this.gameObjects.add(this.player);

        GameObjectBuilder triBuilder = new GameObjectBuilderImpl(new Vector2(this.w.getWidth(), this.w.getHeight()).divide(2.0), false);
        triBuilder = triBuilder.collider(new MeshCollider(Controller.regularPolygon(3), 50.0, 0.0));
        this.gameObjects.add(triBuilder.build());
        
        GameObjectBuilder circleBuilder = new GameObjectBuilderImpl(new Vector2(this.w.getWidth(), this.w.getHeight()).divide(1.2), false);
        circleBuilder = circleBuilder.collider(new MeshCollider(Controller.regularPolygon(50), 50.0, 0.0));
        this.gameObjects.add(circleBuilder.build());
        
        this.input.addEvent("MoveUp", () -> this.input.isKeyPressed(KeyEvent.VK_W));
        this.input.addEvent("MoveDown", () -> this.input.isKeyPressed(KeyEvent.VK_S));
        this.input.addEvent("MoveLeft", () -> this.input.isKeyPressed(KeyEvent.VK_A));
        this.input.addEvent("MoveRight", () -> this.input.isKeyPressed(KeyEvent.VK_D));

        this.input.addEvent("RotateLeft", () -> this.input.isKeyPressed(KeyEvent.VK_Z));
        this.input.addEvent("RotateRight", () -> this.input.isKeyPressed(KeyEvent.VK_X));
    }

    @Override
    public void render() {
        this.w.update(this.gameObjects);
        this.w.repaint();
    }

    @Override
    public void update(final double deltaTime) {
        this.player.move(this.player.getVelocity().multiply(deltaTime));
    }

    @Override
    public void readInput() {
        Vector2 velocity = this.player.getVelocity();
        if (this.input.getEvent("MoveUp")) {
            velocity = velocity.add(Vector2.up());
        }
        if (this.input.getEvent("MoveDown")) {
            velocity = velocity.add(Vector2.down());
        }
        if (this.input.getEvent("MoveLeft")) {
            velocity = velocity.add(Vector2.left());
        }
        if (this.input.getEvent("MoveRight")) {
            velocity = velocity.add(Vector2.right());
        }
        if (this.input.getEvent("RotateLeft")) {
            this.player.rotate(this.rotSpeed);
        }
        if (this.input.getEvent("RotateRight")) {
            this.player.rotate(-this.rotSpeed);
        }
        this.player.setVelocity(velocity.normalize().multiply(this.speed));
    }

    @Override
    public List<GameObject> getGameObjects() {
        return this.gameObjects;
    }
}
