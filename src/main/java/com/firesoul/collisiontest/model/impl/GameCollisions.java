package com.firesoul.collisiontest.model.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.firesoul.collisiontest.controller.impl.Controller;
import com.firesoul.collisiontest.controller.impl.InputController;
import com.firesoul.collisiontest.model.api.CollisionTest;
import com.firesoul.collisiontest.model.api.GameObject;
import com.firesoul.collisiontest.model.api.GameObjectBuilder;
import com.firesoul.collisiontest.model.api.GameObjectFactory;
import com.firesoul.collisiontest.model.util.Vector2;
import com.firesoul.collisiontest.view.impl.Renderer;

public class GameCollisions implements CollisionTest {

    private static final int TILE_SIZE = 20;

    private final Renderer w;
    private final InputController input;

    private final List<GameObject> gameObjects = new ArrayList<>();
    private final List<GameObject> projectiles = new ArrayList<>();

    private final GameObjectFactory gf = new GameObjectFactoryImpl();
    private GameObject player;

    public GameCollisions(final Renderer w) {
        this.w = w;
        this.input = w.getInput();
        this.addGameObjects();
    }

    private void addGameObjects() {
        final Vector2 playerPosition = new Vector2(this.w.getWidth(), this.w.getHeight()).divide(2.0);
        this.player = this.gf.player(playerPosition, this.input, this);
        this.gameObjects.add(this.gf.ballEnemy(playerPosition.add(Vector2.one().multiply(200))));
        this.gameObjects.add(this.player);

        final List<Vector2> blockPoints = Controller.regularPolygon(4);
        for (int x = 1; x < 25; x++) {
            GameObjectBuilder blockBuilder = new BlockBuilder(new Vector2(x*GameCollisions.TILE_SIZE*2, 600));
            blockBuilder = blockBuilder.collider(new MeshCollider(blockPoints, 28.0, Math.PI/4));
            // blockBuilder = blockBuilder.image(this.blockImage);
            this.gameObjects.add(blockBuilder.build());
        }
    }

    @Override
    public void render() {
        this.w.update(this.gameObjects);
        this.w.repaint();
    }

    @Override
    public void update(final double deltaTime) {
        this.gameObjects.forEach(t -> t.update(deltaTime));
        this.gameObjects.addAll(this.projectiles);
        this.projectiles.clear();

        for (final GameObject g : this.gameObjects) {
            final Vector2 pos = g.getPosition();
            if (pos.x() < -w.getWidth() || pos.x() > w.getWidth()*2 || pos.y() < -w.getHeight() || pos.y() > w.getHeight()*2) {
                g.destroy();
            }
        }

        final Iterator<GameObject> it = this.gameObjects.iterator();
        while (it.hasNext()) {
            final GameObject g = it.next();
            if (!g.isActive()) {
                it.remove();
            }
        }
    }

    @Override
    public List<GameObject> getGameObjects() {
        return this.gameObjects;
    }

    public void spawnProjectile() {
        this.projectiles.add(this.gf.projectile(this.player.getPosition(), 10.0));
    }

    @Override
    public void readInput() {
        ((Player) this.player).readInput();
    }
}
