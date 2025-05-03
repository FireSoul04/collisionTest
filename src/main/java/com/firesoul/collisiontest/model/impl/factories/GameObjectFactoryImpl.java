package com.firesoul.collisiontest.model.impl.factories;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.firesoul.collisiontest.controller.impl.GameCore;
import com.firesoul.collisiontest.controller.impl.InputController;
import com.firesoul.collisiontest.model.api.*;
import com.firesoul.collisiontest.model.impl.gameobjects.EnemyImpl;
import com.firesoul.collisiontest.model.impl.gameobjects.GameObjectImpl;
import com.firesoul.collisiontest.model.impl.gameobjects.Player;
import com.firesoul.collisiontest.model.impl.gameobjects.Projectile;
import com.firesoul.collisiontest.model.impl.gameobjects.colliders.MeshCollider;
import com.firesoul.collisiontest.model.util.Vector2;
import com.firesoul.collisiontest.view.api.Drawable;
import com.firesoul.collisiontest.view.impl.SwingSprite;

public class GameObjectFactoryImpl implements GameObjectFactory {

    private final Level world;

    public GameObjectFactoryImpl(final Level world) {
        this.world = world;
    }

    @Override
    public Player player(final Vector2 position, final InputController input) {
        final List<Vector2> colliderPoints = List.of(
            new Vector2(-10, 25),
            new Vector2(-10, -25),
            new Vector2(10, -25),
            new Vector2(10, 25)
        );
        final Collider collider = new MeshCollider(colliderPoints, 1.0, 0);
        final Map<String, Drawable> sprites = Map.of(
            "idle", new SwingSprite("player", position),
            "damage", new SwingSprite("player_damage", position)
        );
        collider.setPosition(position);
        return new Player(position, this.world, Optional.of(collider), sprites, input);
    }

    @Override
    public GameObject projectile(final Vector2 position, final double speed) {
        final List<Vector2> colliderPoints = List.of(
            new Vector2(-2.0, 1.0),
            new Vector2(2.0, 1.0),
            new Vector2(2.0, -1.0),
            new Vector2(-2.0, -1.0)
        );
        final Collider collider = new MeshCollider(colliderPoints, 2.0);
        final Drawable sprite = new SwingSprite("projectile", position);
        collider.setPosition(position);
        return new Projectile(position, this.world, Optional.of(collider), Optional.of(sprite), speed);
    }

    @Override
    public GameObject ballEnemy(final Vector2 position) {
        final Collider collider = new MeshCollider(GameCore.regularPolygon(50), 17.0, 0);
        final Map<String, Drawable> sprites = Map.of(
                "idle", new SwingSprite("enemy", position),
                "damage", new SwingSprite("enemy_damage", position)
        );
        collider.setPosition(position);
        return new EnemyImpl(position, true, this.world, Optional.of(collider), sprites, 200, 10, () -> null);
    }

    @Override
    public GameObject block(final Vector2 position) {
        final Collider collider = new MeshCollider(GameCore.regularPolygon(4), 14.0, Math.PI/4);
        // final Drawable sprite = null;
        collider.setPosition(position);
        return new GameObjectImpl(position, false, this.world, Optional.of(collider), Optional.empty());
    }
}
