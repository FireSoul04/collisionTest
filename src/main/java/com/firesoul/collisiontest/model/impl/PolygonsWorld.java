package com.firesoul.collisiontest.model.impl;

import com.firesoul.collisiontest.controller.api.GameController;
import com.firesoul.collisiontest.controller.impl.GameCore;
import com.firesoul.collisiontest.controller.impl.InputController;
import com.firesoul.collisiontest.model.api.GameObject;
import com.firesoul.collisiontest.model.api.GameObjectBuilder;
import com.firesoul.collisiontest.model.api.Level;
import com.firesoul.collisiontest.model.api.gameobjects.Camera;
import com.firesoul.collisiontest.model.impl.physics.colliders.MeshCollider;
import com.firesoul.collisiontest.model.util.Vector2;
import com.firesoul.collisiontest.view.api.DrawableFactory;
import com.firesoul.collisiontest.view.api.Renderer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PolygonsWorld implements Level {

    private final List<GameObject> gameObjects = new ArrayList<>();
    private final GameController controller;
    private final InputController input;

    private final double speed = 1.0;
    private final double rotSpeed = 0.1;

    private GameObject player;

    public PolygonsWorld(final GameController controller) {
        this.controller = controller;
        this.input = controller.getInput();
        this.addGameObjects();
    }

    @Override
    public void update(final double deltaTime) {
        this.readInput();
        this.player.move(this.player.getVelocity().multiply(deltaTime));
    }

    @Override
    public List<GameObject> getGameObjects() {
        return Collections.unmodifiableList(this.gameObjects);
    }

    @Override
    public double getWidth() {
        return 0;
    }

    @Override
    public double getHeight() {
        return 0;
    }

    @Override
    public Vector2 getPlayerPosition() {
        return this.player.getPosition();
    }

    @Override
    public Camera getCamera() {
        return this.controller.getCamera();
    }

    @Override
    public DrawableFactory getDrawableFactory() {
        return this.controller.getDrawableFactory();
    }

    @Override
    public void instanciate(final GameObject gameObject) {
        this.gameObjects.add(gameObject);
    }

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

    private void addGameObjects() {
        final Vector2 playerPosition = new Vector2(this.getWidth(), this.getHeight()).divide(4.0);
        GameObjectBuilder playerBuilder = new GameObjectBuilderImpl(playerPosition, true, this);
        playerBuilder = playerBuilder.collider(new MeshCollider(playerPosition, GameCore.regularPolygon(5), 50.0));
        this.player = playerBuilder.build();
        this.instanciate(this.player);

        final Vector2 triPosition = new Vector2(this.getWidth(), this.getHeight()).divide(2.0);
        GameObjectBuilder triBuilder = new GameObjectBuilderImpl(triPosition, false, this);
        triBuilder = triBuilder.collider(new MeshCollider(triPosition, GameCore.regularPolygon(3), 50.0));
        this.instanciate(triBuilder.build());

        final Vector2 circlePosition = new Vector2(this.getWidth(), this.getHeight()).divide(1.2);
        GameObjectBuilder circleBuilder = new GameObjectBuilderImpl(circlePosition, false, this);
        circleBuilder = circleBuilder.collider(new MeshCollider(circlePosition, GameCore.regularPolygon(50), 50.0));
        this.instanciate(circleBuilder.build());
    }
}
