package com.firesoul.collisiontest.model.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.firesoul.collisiontest.controller.impl.Controller;
import com.firesoul.collisiontest.controller.impl.InputController;
import com.firesoul.collisiontest.model.api.*;
import com.firesoul.collisiontest.model.util.Vector2;
import com.firesoul.collisiontest.view.api.Drawable;
import com.firesoul.collisiontest.view.api.Renderer;
import com.firesoul.collisiontest.view.impl.SwingSprite;

public class GameObjectFactoryImpl implements GameObjectFactory {

    private final Renderer renderer;

    public GameObjectFactoryImpl(final Renderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public Player player(final Vector2 position, final InputController input, final GameCollisions world) {
        final List<Vector2> colliderPoints = List.of(
            new Vector2(-1.0, 2.5),
            new Vector2(-1.0, -2.5),
            new Vector2(1.0, -2.5),
            new Vector2(1.0, 2.5)
        );
        final Collider collider = new MeshCollider(colliderPoints, 20.0, 0);
        final Collider swordCollider = new MeshCollider(Controller.regularPolygon(4), 16.0, Math.PI/2);
        final Map<String, Drawable> sprites = Map.of(
            "idle", new SwingSprite("player", position, 0.0, this.renderer),
            "damage", new SwingSprite("player_damage", position, 0.0, this.renderer)
        );
        final Map<String, Drawable> swordSprites = Map.of(
            "idle", new SwingSprite("sword1", Vector2.zero(), 0.0, this.renderer),
            "swing", new SwingSprite("sword_swing", Vector2.zero(), 0.0, this.renderer)
        );
        final Player player = new Player(position, 0.0, Optional.of(collider), sprites, input, world);
        final Weapon sword = new Sword(player, new Vector2(35.0, -20.0), new Vector2(45.0, -10.0), 0.0, swordCollider, swordSprites);
        player.equip(sword);
        collider.attachGameObject(player);
        swordCollider.attachGameObject(sword);
        return player;
    }

    @Override
    public GameObject projectile(final Vector2 position, final double speed) {
        final List<Vector2> colliderPoints = List.of(
            new Vector2(-2.0, 1.0),
            new Vector2(2.0, 1.0),
            new Vector2(2.0, -1.0),
            new Vector2(-2.0, -1.0)
        );
        final Collider collider = new MeshCollider(colliderPoints, 4.0, 0.0);
        final Drawable sprite = new SwingSprite("projectile", position, 0.0, this.renderer);
        final GameObject projectile = new Projectile(position, speed, Optional.of(collider), Optional.of(sprite), speed);
        collider.attachGameObject(projectile);
        return projectile;
    }

    @Override
    public GameObject ballEnemy(final Vector2 position) {
        final Collider collider = new MeshCollider(Controller.regularPolygon(50), 35.0, 0);
        final Map<String, Drawable> sprites = Map.of(
                "idle", new SwingSprite("enemy", position, 0.0, this.renderer),
                "damage", new SwingSprite("enemy_damage", position, 0.0, this.renderer)
        );
        final Enemy enemy = new EnemyImpl(position, 0.0, true, Optional.of(collider), sprites, 200, 10, () -> null);
        collider.attachGameObject(enemy);
        return enemy;
    }

    @Override
    public GameObject block(final Vector2 position) {
        final Collider collider = new MeshCollider(Controller.regularPolygon(4), 28.0, Math.PI/4);
        // final Drawable sprite = null;
        final GameObject block = new GameObjectImpl(position, 0.0, false, Optional.of(collider), Optional.empty());
        collider.attachGameObject(block);
        return block;
    }
}
