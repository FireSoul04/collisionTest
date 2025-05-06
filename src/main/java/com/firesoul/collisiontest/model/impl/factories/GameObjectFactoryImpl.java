package com.firesoul.collisiontest.model.impl.factories;

import java.awt.Color;
import java.util.Map;
import java.util.Optional;

import com.firesoul.collisiontest.controller.impl.GameCore;
import com.firesoul.collisiontest.controller.impl.InputController;
import com.firesoul.collisiontest.model.api.*;
import com.firesoul.collisiontest.model.api.factories.GameObjectFactory;
import com.firesoul.collisiontest.model.api.gameobjects.Enemy;
import com.firesoul.collisiontest.model.api.physics.Collider;
import com.firesoul.collisiontest.model.impl.gameobjects.*;
import com.firesoul.collisiontest.model.impl.gameobjects.bars.GameBar;
import com.firesoul.collisiontest.model.impl.gameobjects.bars.StaticGameBar;
import com.firesoul.collisiontest.model.impl.physics.colliders.BoxCollider;
import com.firesoul.collisiontest.model.impl.physics.colliders.MeshCollider;
import com.firesoul.collisiontest.model.util.Vector2;
import com.firesoul.collisiontest.view.api.Drawable;
import com.firesoul.collisiontest.view.api.DrawableFactory;

public class GameObjectFactoryImpl implements GameObjectFactory {

    private final DrawableFactory df;
    private final Level world;

    public GameObjectFactoryImpl(final Level world) {
        this.df = world.getDrawableFactory();
        this.world = world;
    }

    @Override
    public Player player(final Vector2 position, final InputController input) {
        final Vector2 size = new Vector2(20.0, 50.0);
        final Collider collider = new BoxCollider(position.subtract(size.divide(2.0)), size.x(), size.y());
        final Map<String, Drawable> sprites = Map.of(
            "idle", this.df.spriteByName("player", position),
            "damage", this.df.spriteByName("player_damage", position)
        );
        final GameBar lifeBar = new StaticGameBar(Vector2.one(), this.world,
            this.df.staticBar(20, 10, Color.RED.getRGB()), 12
        );
        this.world.instanciate(lifeBar);
        return new Player(position, this.world, Optional.of(collider), sprites, input, lifeBar);
    }

    @Override
    public GameObject projectile(final Vector2 position, final double speed) {
        final Vector2 size = new Vector2(10.0, 5.0);
        final Collider collider = new BoxCollider(position.subtract(size.divide(2.0)), size.x(), size.y());
        final Drawable sprite = this.df.spriteByName("projectile", position);
        sprite.mirrorX(Math.signum(speed));
        return new Projectile(position, this.world, Optional.of(collider), Optional.of(sprite), speed);
    }

    @Override
    public GameObject groundEnemy(final Vector2 position) {
        final Collider collider = new MeshCollider(position, GameCore.regularPolygon(8), 17.0, 0);
        final Map<String, Drawable> sprites = Map.of(
            "idle", this.df.spriteByName("enemy", position),
            "damage", this.df.spriteByName("enemy_damage", position)
        );
        return new EnemyImpl(position, true, this.world, Optional.of(collider), sprites,
            200, 10, new Vector2(0.0, 0.25),
            e -> {
                final Vector2 goTo = this.world.getPlayerPosition().subtract(e.getPosition()).normalize();
                e.move(new Vector2(goTo.x(), 0.0));
            }
        );
    }

    @Override
    public GameObject flyingEnemy(final Vector2 position, final double range, final double speed) {
        final Collider collider = new MeshCollider(position, GameCore.regularPolygon(8), 17.0, 0);
        final Map<String, Drawable> sprites = Map.of(
            "idle", this.df.spriteByName("enemy", position),
            "damage", this.df.spriteByName("enemy_damage", position)
        );
        return new EnemyImpl(position, true, this.world, Optional.of(collider), sprites,
            200, 10, Vector2.zero(),
            new EnemyImpl.EnemyBehavior() {
                final double eRange = range;
                final double eSpeed = speed;
                double step = 0.0;

                public void behave(final Enemy e) {
                    e.move(new Vector2(0.0, Math.sin(this.step)).multiply(this.eRange));
                    this.step += this.eSpeed;
                }
            }
        );
    }

    @Override
    public GameObject block(final Vector2 position) {
        final Vector2 size = new Vector2(20.0, 20.0);
        final Collider collider = new BoxCollider(position.subtract(size.divide(2.0)), size.x(), size.y());
        // final Drawable sprite = null;
        return new GameObjectImpl(position, false, this.world, Optional.of(collider), Optional.empty());
    }
}
