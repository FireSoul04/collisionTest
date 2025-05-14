package com.firesoul.collisiontest.model.impl.factories;

import java.awt.Color;
import java.util.Map;
import java.util.Optional;

import com.firesoul.collisiontest.controller.api.EventManager;
import com.firesoul.collisiontest.controller.api.loader.DrawableLoader;
import com.firesoul.collisiontest.controller.impl.GameCore;
import com.firesoul.collisiontest.model.api.*;
import com.firesoul.collisiontest.model.api.drawable.Drawable;
import com.firesoul.collisiontest.model.api.factories.GameObjectFactory;
import com.firesoul.collisiontest.model.api.gameobjects.Enemy;
import com.firesoul.collisiontest.model.api.physics.Collider;
import com.firesoul.collisiontest.model.impl.gameobjects.*;
import com.firesoul.collisiontest.model.impl.gameobjects.bars.AttachedBar;
import com.firesoul.collisiontest.model.impl.gameobjects.bars.GameBar;
import com.firesoul.collisiontest.model.impl.gameobjects.bars.StaticGameBar;
import com.firesoul.collisiontest.model.impl.gameobjects.weapons.Projectile;
import com.firesoul.collisiontest.model.impl.physics.colliders.BoxCollider;
import com.firesoul.collisiontest.model.impl.physics.colliders.MeshCollider;
import com.firesoul.collisiontest.model.util.Vector2;

public class GameObjectFactoryImpl implements GameObjectFactory {

    private final DrawableLoader dl;
    private final Level world;

    public GameObjectFactoryImpl(final DrawableLoader dl, final Level world) {
        this.dl = dl;
        this.world = world;
    }

    @Override
    public Player player(final Vector2 position, final EventManager<String> input) {
        final Vector2 size = new Vector2(20.0, 50.0);
        final Collider collider = new BoxCollider(position.subtract(size.divide(2.0)), size.x(), size.y());
        final Map<String, Drawable> sprites = Map.of(
            "idle", this.dl.loadSpriteFromSystem("player"),
            "damage", this.dl.loadSpriteFromSystem("player_damage")
        );
        final int life = 12;
        final GameBar lifeBar = new StaticGameBar(Vector2.one(), this.world,
            this.dl.loadStaticBar(Vector2.one(), 40, 10, Color.GREEN.getRGB()), life, false
        );
        return new Player(position, this.world, Optional.of(collider), sprites, input, lifeBar);
    }

    @Override
    public GameObject projectile(final Vector2 position, final double speed) {
        final Vector2 size = new Vector2(10.0, 5.0);
        final Collider collider = new BoxCollider(position.subtract(size.divide(2.0)), size.x(), size.y());
        final Drawable sprite = this.dl.loadSpriteFromSystem("projectile");
        sprite.mirrorX(Math.signum(speed));
        return new Projectile(position, this.world, Optional.of(collider), Optional.of(sprite), speed);
    }

    @Override
    public GameObject groundEnemy(final Vector2 position) {
        final Collider collider = new MeshCollider(position, GameCore.regularPolygon(8), 17.0, 0);
        final Map<String, Drawable> sprites = Map.of(
            "idle", this.dl.loadSpriteFromSystem("enemy"),
            "damage", this.dl.loadSpriteFromSystem("enemy_damage")
        );
        final int life = 10;
        return new EnemyImpl(position, true, this.world, Optional.of(collider), sprites,
            200, life, new Vector2(0.0, 0.25),
            e -> {
                final Vector2 goTo = this.world.getPlayerPosition().subtract(e.getPosition()).normalize();
                e.move(new Vector2(goTo.x(), 0.0));
            }, new AttachedBar(g -> g.getSprite()
                .map(t -> new Vector2(0.0, -t.getHeight() * 0.75))
                .orElse(Vector2.zero()), this.world, this.dl.loadDynamicBar(30, 5, Color.RED.getRGB()), life, false)
        );
    }

    @Override
    public GameObject flyingEnemy(final Vector2 position, final double range, final double speed) {
        final Collider collider = new MeshCollider(position, GameCore.regularPolygon(8), 17.0, 0);
        final Map<String, Drawable> sprites = Map.of(
            "idle", this.dl.loadSpriteFromSystem("enemy"),
            "damage", this.dl.loadSpriteFromSystem("enemy_damage")
        );
        final int life = 10;
        return new EnemyImpl(position, true, this.world, Optional.of(collider), sprites,
            200, life, Vector2.zero(),
            new EnemyImpl.EnemyBehavior() {
                final double eRange = range;
                final double eSpeed = speed;
                double step = 0.0;

                public void behave(final Enemy e) {
                    e.move(new Vector2(0.0, Math.sin(this.step)).multiply(this.eRange));
                    this.step += this.eSpeed;
                }
            }, new AttachedBar(g -> g.getSprite()
                .map(t -> new Vector2(0.0, -t.getHeight() * 0.75))
                .orElse(Vector2.zero()), this.world, this.dl.loadDynamicBar(30, 5, Color.RED.getRGB()), life, false)
        );
    }

    @Override
    public GameObject block(final Vector2 position) {
        final Vector2 size = new Vector2(20.0, 20.0);
        final Collider collider = new BoxCollider(position.subtract(size.divide(2.0)), size.x(), size.y());
        final Drawable sprite = this.dl.loadRectangleBorder(position.subtract(size.divide(2.0)), (int) size.x(), (int) size.y(), Color.BLACK.getRGB());
        return new GameObjectImpl(position, false, this.world, Optional.of(collider), Optional.of(sprite));
    }
}
