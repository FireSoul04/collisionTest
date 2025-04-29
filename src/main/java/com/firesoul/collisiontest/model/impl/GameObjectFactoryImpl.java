package com.firesoul.collisiontest.model.impl;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;

import com.firesoul.collisiontest.controller.impl.Controller;
import com.firesoul.collisiontest.controller.impl.InputController;
import com.firesoul.collisiontest.model.api.GameObject;
import com.firesoul.collisiontest.model.api.Collider;
import com.firesoul.collisiontest.model.api.Enemy;
import com.firesoul.collisiontest.model.api.GameObjectFactory;
import com.firesoul.collisiontest.model.util.Vector2;

public class GameObjectFactoryImpl implements GameObjectFactory {
    
    private class Sword extends GameObjectImpl {

        public Sword(final Vector2 position, final double orientation, final Optional<Collider> collider, final Optional<Image> image) {
            super(position, orientation, true, collider, image);
            this.getCollider().get().setSolid(false);
        }

        @Override
        public void onCollide(final Collider collidedShape, final Vector2 collisionDirection, final double collisionTime) {
            final GameObject g = collidedShape.getAttachedGameObject();
            if (g instanceof Enemy e && this.getCollider().get().isSolid()) {
                e.takeDamage(3);
            }
        }
    }

    @Override
    public GameObject player(final Vector2 position, final InputController input, final GameCollisions world) {
        final List<Vector2> colliderPoints = List.of(
            new Vector2(-1.0, 2.5),
            new Vector2(-1.0, -2.5),
            new Vector2(1.0, -2.5),
            new Vector2(1.0, 2.5)
        );
        final List<Vector2> swordColliderPoints = List.of(
            new Vector2(-0.5, 2.0),
            new Vector2(0.5, 2.0),
            new Vector2(0.5, -3.0),
            new Vector2(-0.5, -3.0)
        );
        final Collider collider = new MeshCollider(colliderPoints, 20.0, 0);
        final Collider swordCollider = new MeshCollider(swordColliderPoints, 16.0, 0);
        Image swordImage = null;
        try {
            swordImage = ImageIO.read(new File("src/main/resources/sword.png"));
        } catch (Exception e) {
            System.out.println("Could not load sword sprite");
            System.exit(1);
        }
        final GameObject sword = new Sword(position.add(new Vector2(35.0, 0.0)), Math.PI/3, Optional.of(swordCollider), Optional.of(swordImage));
        Image image = null;
        try {
            image = ImageIO.read(new File("src/main/resources/player.png"));
        } catch (Exception e) {
            System.out.println("Could not load player sprite");
            System.exit(1);
        }
        final GameObject player = new Player(position, 0.0, Optional.of(collider), Optional.of(image), sword, input, world);
        collider.attachGameObject(player);
        swordCollider.attachGameObject(sword);
        return player;
    }

    @Override
    public GameObject projectile(final Vector2 position, final double speed) {
        Image image = null;
        try {
            image = ImageIO.read(new File("src/main/resources/projectile.png"));
        } catch (IOException e) {
            System.out.println("Could not load projectile sprite");
            System.exit(1);
        }
        final List<Vector2> colliderPoints = List.of(
            new Vector2(-2.0, 1.0),
            new Vector2(2.0, 1.0),
            new Vector2(2.0, -1.0),
            new Vector2(-2.0, -1.0)
        );
        final Collider collider = new MeshCollider(colliderPoints, 4.0, 0.0);
        final GameObject projectile = new Projectile(position, speed, Optional.of(collider), Optional.of(image), speed);
        collider.attachGameObject(projectile);
        return projectile;
    }

    @Override
    public GameObject ballEnemy(final Vector2 position) {
        final Collider collider = new MeshCollider(Controller.regularPolygon(50), 20.0, 0);
        Image image = null;
        try {
            image = ImageIO.read(new File("src/main/resources/enemy.png"));
        } catch (IOException e) {
            System.out.println("Could not load enemy sprite");
            System.exit(1);
        }
        final Enemy enemy = new EnemyImpl(position, 0.0, true, Optional.of(collider), Optional.of(image), 250, 10, () -> {});
        collider.attachGameObject(enemy);
        return enemy;
    }
}
