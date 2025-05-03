package com.firesoul.collisiontest.model.impl;

import com.firesoul.collisiontest.model.api.*;
import com.firesoul.collisiontest.model.api.gameobjects.Weapon;
import com.firesoul.collisiontest.model.impl.gameobjects.Player;
import com.firesoul.collisiontest.model.util.Vector2;
import com.firesoul.collisiontest.view.api.Renderer;

import java.util.*;

public class LevelImpl implements Level {

    private static final int TILE_SIZE = 20;

    private final int width = 1600;
    private final int height = 1200;

    private final List<GameObject> gameObjects = new ArrayList<>();
    private final List<GameObject> projectiles = new ArrayList<>();

    private final Renderer renderer;
    private final GameObjectFactory gf;
    private Player player;

    public LevelImpl(final Renderer renderer) {
        this.gf = new GameObjectFactoryImpl(renderer);
        this.renderer = renderer;
        this.addGameObjects();

        this.renderer.getCamera().setBoundsX(this.width);
        this.renderer.getCamera().setBoundsY(this.height);
    }

    @Override
    public void update(final double deltaTime) {
        this.checkCollisions(deltaTime);
        this.gameObjects.forEach(t -> t.update(deltaTime));
        this.gameObjects.addAll(this.projectiles);
        this.projectiles.clear();
        this.renderer.getCamera().setPosition(this.player.getPosition().subtract(
                new Vector2(this.renderer.getGameWidth(), this.renderer.getGameHeight())
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
    public void spawnProjectile(final Vector2 position, final Vector2 velocity) {
        this.projectiles.add(this.gf.projectile(position, velocity.x()));
    }

    private void checkCollisions(final double deltaTime) {
        final List<Collider> colliders = this.gameObjects.stream()
                .map(GameObject::getCollider)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        final List<Collider> dynamicColliders = colliders.stream()
                .filter(t -> t.getAttachedGameObject().isDynamic())
                .toList();
        for (final Collider c1 : dynamicColliders) {
            boolean collided = false;
            final Map<Collider, Double> collidersByCollisionTime = new HashMap<>();
            for (final Collider c2 : colliders.stream().filter(t -> !t.equals(c1)).toList()) {
                final CollisionAlgorithms.Swept sw = CollisionAlgorithms.sweptAABB(c1, c2, deltaTime);
                collided = sw != null;
                // collided = Controller.SAT(s1, s2);
                if (collided) {
                    collidersByCollisionTime.put(c2, sw.time());
                    c1.addCollided(c2);
                } else {
                    c1.removeCollided(c2);
                }
            }

            for (var x : collidersByCollisionTime.entrySet().stream().sorted((a, b) -> Double.compare(a.getValue(), b.getValue())).toList()) {
                CollisionAlgorithms.resolveSweptAABB(c1, x.getKey(), deltaTime);
            }
        }
    }

    private void addGameObjects() {
        final Vector2 playerPosition = new Vector2(this.renderer.getGameWidth(), this.renderer.getGameHeight()).divide(2.0);
        this.player = this.gf.player(playerPosition, this.renderer.getInput(), this);
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
            this.gameObjects.add(this.gf.block(new Vector2(x * TILE_SIZE, 600)));
        }
    }
}
