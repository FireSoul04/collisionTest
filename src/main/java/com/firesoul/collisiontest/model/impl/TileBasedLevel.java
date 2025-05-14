package com.firesoul.collisiontest.model.impl;

import com.firesoul.collisiontest.controller.impl.GameCore;
import com.firesoul.collisiontest.model.api.*;
import com.firesoul.collisiontest.model.api.factories.GameObjectFactory;
import com.firesoul.collisiontest.model.api.gameobjects.Camera;
import com.firesoul.collisiontest.model.api.physics.Collider;
import com.firesoul.collisiontest.model.impl.factories.GameObjectFactoryImpl;
import com.firesoul.collisiontest.model.impl.gameobjects.Player;
import com.firesoul.collisiontest.model.util.GameTimer;
import com.firesoul.collisiontest.model.util.Vector2;
import java.util.*;

public class TileBasedLevel implements Level {

    public static final int TILE_SIZE = 20;

    private final int width;
    private final int height;

    private final Set<GameTimer> timers = new HashSet<>();
    private final List<GameObject> gameObjects = new ArrayList<>();
    private final Queue<GameObject> gameObjectsQ = new ArrayDeque<>();

    private final GameCore controller;
    private final GameObjectFactory gf;
    private Player player;

    public TileBasedLevel(final int width, final int height, final GameCore controller) {
        this.width = width;
        this.height = height;
        this.controller = controller;
        this.gf = new GameObjectFactoryImpl(controller.getDrawableLoader(), this);
        controller.getEventManager().addEvent("GameOver", () -> !this.player.isActive());
    }

    public TileBasedLevel(final GameCore controller) {
        this(1600, 1200, controller);
    }

    @Override
    public void update(final double deltaTime) {
        this.player.readInput();
        this.checkCollisions(deltaTime);
        this.gameObjects.addAll(this.gameObjectsQ);
        this.gameObjectsQ.clear();
        this.gameObjects.forEach(g -> g.update(deltaTime));
        this.controller.getCamera().setPosition(this.getPlayerPosition().subtract(
            new Vector2(this.controller.getGameWidth(), this.controller.getGameHeight())
                .divide(2.0)
        ));
        this.destroyOutOfBoundsObjects();
    }

    @Override
    public void pause() {
        this.timers.forEach(GameTimer::pause);
    }

    @Override
    public void unPause() {
        this.timers.forEach(GameTimer::unPause);
    }

    private void destroyOutOfBoundsObjects() {
        for (final GameObject g : this.gameObjects) {
            final Vector2 pos = g.getPosition();
            final boolean isOutOfBounds =
                pos.x() < 0 ||
                    pos.x() > this.width ||
                    pos.y() < 0 ||
                    pos.y() > this.height;
            if (isOutOfBounds) {
                g.destroy();
            }
        }
        this.gameObjects.removeIf(g -> !g.isActive());
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
    public GameObjectFactory getGameObjectFactory() {
        return this.gf;
    }

    @Override
    public void instanciate(final GameObject gameObject) {
        if (gameObject instanceof Player p) {
            this.player = p;
        }
        this.gameObjectsQ.add(gameObject);
    }

    @Override
    public void addTimer(final GameTimer timer) {
        this.timers.add(timer);
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
}
