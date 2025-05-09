package com.firesoul.collisiontest.model.impl;

import com.firesoul.collisiontest.controller.impl.GameCore;
import com.firesoul.collisiontest.controller.impl.InputController;
import com.firesoul.collisiontest.model.api.*;
import com.firesoul.collisiontest.model.api.factories.GameObjectFactory;
import com.firesoul.collisiontest.model.api.factories.WeaponFactory;
import com.firesoul.collisiontest.model.api.gameobjects.Camera;
import com.firesoul.collisiontest.model.api.gameobjects.Weapon;
import com.firesoul.collisiontest.model.api.physics.Collider;
import com.firesoul.collisiontest.model.impl.factories.GameObjectFactoryImpl;
import com.firesoul.collisiontest.model.impl.factories.WeaponFactoryImpl;
import com.firesoul.collisiontest.model.impl.gameobjects.Player;
import com.firesoul.collisiontest.model.util.Vector2;
import java.util.*;

public class LevelImpl implements Level {

    private static final int TILE_SIZE = 20;

    private final int width = 1600;
    private final int height = 1200;

    private final List<GameObject> gameObjects = new ArrayList<>();
    private final Queue<GameObject> gameObjectsQ = new ArrayDeque<>();

    private final GameCore controller;
    private final GameObjectFactory gf;
    private Player player;

    public LevelImpl(final GameCore controller) {
        this.controller = controller;
        this.gf = new GameObjectFactoryImpl(controller.getDrawableLoader(), this);
        this.addGameObjects(controller.getInput());
    }

    @Override
    public void update(final double deltaTime) {
        this.checkCollisions(deltaTime);
        this.gameObjects.forEach(t -> t.update(deltaTime));
        this.gameObjects.addAll(this.gameObjectsQ);
        this.gameObjectsQ.clear();
        this.controller.getCamera().setPosition(this.getPlayerPosition().subtract(
            new Vector2(this.controller.getGameWidth(), this.controller.getGameHeight())
                .divide(2.0)
        ));

        for (final GameObject g : this.gameObjects) {
            final Vector2 pos = g.getPosition();
            final boolean isOutOfBounds =
                pos.x() < -this.width ||
                pos.x() > this.height * 2 ||
                pos.y() < -this.width ||
                pos.y() > this.height * 2;
            if (isOutOfBounds) {
                g.destroy();
            }
        }
        this.gameObjects.removeIf(g -> !g.isActive());
        this.player.readInput();
    }

    @Override
    public List<GameObject> getGameObjects() {
        return this.gameObjects;
    }

    @Override
    public double getWidth() {
        return this.width;
    }

    @Override
    public double getHeight() {
        return this.height;
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
    public void instanciate(final GameObject gameObject) {
        this.gameObjectsQ.add(gameObject);
    }

    private void checkCollisions(final double deltaTime) {
        final List<GameObject> gameObjects = this.gameObjects.stream()
            .filter(g -> g.getCollider().isPresent())
            .toList();
        final List<GameObject> dynamicGameObjects = gameObjects.stream()
            .filter(GameObject::isDynamic)
            .toList();
        for (final GameObject g1 : dynamicGameObjects) {
            final Map<GameObject, Double> collidersByCollisionTime = new HashMap<>();
            for (final GameObject g2 : gameObjects.stream().filter(t -> !t.equals(g1)).toList()) {
                final Collider c = g1.getCollider().orElseThrow();
                final CollisionAlgorithms.Collision collision = CollisionAlgorithms.sweptAABB(g1, g2, deltaTime);
                boolean collided = collision != null;
                if (collided) {
                    collidersByCollisionTime.put(g2, collision.time());
                    c.addCollided(g2);
                } else {
                    c.removeCollided(g2);
                }
            }
            this.resolveCollisions(g1, collidersByCollisionTime, deltaTime);
        }
    }

    private void resolveCollisions(final GameObject g, final Map<GameObject, Double> collisions, final double deltaTime) {
        for (final Map.Entry<GameObject, Double> collision : collisions
            .entrySet()
            .stream()
            .sorted(Comparator.comparingDouble(Map.Entry::getValue))
            .toList()
        ) {
            CollisionAlgorithms.resolveSweptAABB(g, collision.getKey(), deltaTime);
        }
    }

    private void addGameObjects(final InputController input) {
        final Vector2 playerPosition = new Vector2(this.getWidth(), this.getHeight()).divide(4.0);
        this.player = this.gf.player(playerPosition, input);
        Objects.requireNonNull(this.player);
       this.gf.flyingEnemy(playerPosition.add(Vector2.one().multiply(100)), 2.0, 0.03);
       this.gf.groundEnemy(playerPosition.add(Vector2.one().multiply(100)));

        final WeaponFactory wf = new WeaponFactoryImpl(controller.getDrawableLoader(), this);
        final Weapon sword = wf.sword(this.player);
        final Weapon gun = wf.gun(this.player);
        this.player.equip(sword);
        this.player.equip(gun);

        this.addBlocks();
    }

    private void addBlocks() {
        for (int x = 1; x < 100; x++) {
            this.gf.block(new Vector2(x * TILE_SIZE, 600));
        }
        for (int y = 1; y < 3; y++) {
            this.gf.block(new Vector2(TILE_SIZE, 600 - y * TILE_SIZE));
        }
        for (int y = 1; y < 3; y++) {
            this.gf.block(new Vector2(99 * TILE_SIZE, 600 - y * TILE_SIZE));
        }
    }
}
