package com.firesoul.collisiontest.model.impl.factories;

import java.util.Map;
import java.util.Optional;

import com.firesoul.collisiontest.controller.impl.GameCore;
import com.firesoul.collisiontest.controller.impl.InputController;
import com.firesoul.collisiontest.model.api.*;
import com.firesoul.collisiontest.model.api.physics.Collider;
import com.firesoul.collisiontest.model.impl.gameobjects.EnemyImpl;
import com.firesoul.collisiontest.model.impl.gameobjects.GameObjectImpl;
import com.firesoul.collisiontest.model.impl.gameobjects.Player;
import com.firesoul.collisiontest.model.impl.gameobjects.Projectile;
import com.firesoul.collisiontest.model.impl.physics.colliders.BoxCollider;
import com.firesoul.collisiontest.model.impl.physics.colliders.MeshCollider;
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
        final Vector2 size = new Vector2(20.0, 50.0);
        final Collider collider = new BoxCollider(position.subtract(size.divide(2.0)), size.x(), size.y());
        final Map<String, Drawable> sprites = Map.of(
            "idle", new SwingSprite("player", position),
            "damage", new SwingSprite("player_damage", position)
        );
        return new Player(position, this.world, Optional.of(collider), sprites, input);
    }

    @Override
    public GameObject projectile(final Vector2 position, final double speed) {
        final Vector2 size = new Vector2(10.0, 5.0);
        final Collider collider = new BoxCollider(position.subtract(size.divide(2.0)), size.x(), size.y());
        final Drawable sprite = new SwingSprite("projectile", position);
        sprite.mirrorX(Math.signum(speed));
        return new Projectile(position, this.world, Optional.of(collider), Optional.of(sprite), speed);
    }

    @Override
    public GameObject ballEnemy(final Vector2 position) {
        final Collider collider = new MeshCollider(position, GameCore.regularPolygon(8), 17.0, 0);
        final Map<String, Drawable> sprites = Map.of(
                "idle", new SwingSprite("enemy", position),
                "damage", new SwingSprite("enemy_damage", position)
        );
        return new EnemyImpl(position, true, this.world, Optional.of(collider), sprites, 200, 10, () -> null);
    }

    @Override
    public GameObject block(final Vector2 position) {
        final Vector2 size = new Vector2(20.0, 20.0);
        final Collider collider = new BoxCollider(position.subtract(size.divide(2.0)), size.x(), size.y());
        // final Drawable sprite = null;
        return new GameObjectImpl(position, false, this.world, Optional.of(collider), Optional.empty());
    }
}
