package com.firesoul.collisiontest.model.impl;

import java.awt.Image;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.firesoul.collisiontest.controller.impl.Controller;
import com.firesoul.collisiontest.controller.impl.InputController;
import com.firesoul.collisiontest.model.api.CollisionTest;
import com.firesoul.collisiontest.model.api.GameObject;
import com.firesoul.collisiontest.model.api.GameObjectBuilder;
import com.firesoul.collisiontest.model.util.Vector2;
import com.firesoul.collisiontest.view.impl.Renderer;

public class GameCollisions implements CollisionTest {

    private static final int TILE_SIZE = 20;

    private final Renderer w;
    private final InputController input;

    private final List<GameObject> gameObjects = new ArrayList<>();
    private final List<GameObject> projectiles = new ArrayList<>();

    private GameObject player;
    
    private Image playerImage;
    private Image swordImage;
    private Image enemyImage;
    private Image projectileImage;

    private final List<Vector2> projectileShape = List.of(
        new Vector2(-2.0, 1.0),
        new Vector2(2.0, 1.0),
        new Vector2(2.0, -1.0),
        new Vector2(-2.0, -1.0)
    );

    public GameCollisions(final Renderer w) {
        this.w = w;
        this.input = w.getInput();

        try {
            this.swordImage = ImageIO.read(new File("src/main/resources/sword.png"));
            this.enemyImage = ImageIO.read(new File("src/main/resources/enemy.png"));
            this.playerImage = ImageIO.read(new File("src/main/resources/player.png"));
            this.projectileImage = ImageIO.read(new File("src/main/resources/projectile.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        this.addGameObjects();
        this.addEvents();
    }

    private void addGameObjects() {
        final List<Vector2> playerShape = List.of(
            new Vector2(-1.0, 2.5),
            new Vector2(-1.0, -2.5),
            new Vector2(1.0, -2.5),
            new Vector2(1.0, 2.5)
        );
        final List<Vector2> swordShape = List.of(
            new Vector2(-0.5, 2.0),
            new Vector2(0.5, 2.0),
            new Vector2(0.5, -3.0),
            new Vector2(-0.5, -3.0)
        );

        GameObjectBuilder swordBuilder = new GameObjectBuilderImpl(new Vector2(this.w.getWidth(), this.w.getHeight()).divide(2.0));
        swordBuilder.collider(new MeshCollider(swordShape, 16.0, 0.0));
        swordBuilder = swordBuilder.image(this.swordImage);
        final GameObject sword = swordBuilder.build();
        sword.move(new Vector2(35.0, 0.0));
        sword.setSolid(false);
        sword.rotate(Math.PI/3);
        this.gameObjects.add(sword);

        GameObjectBuilder enemyBuilder = new GameObjectBuilderImpl(new Vector2(this.w.getWidth(), this.w.getHeight()).divide(1.2));
        enemyBuilder = enemyBuilder.collider(new MeshCollider(Controller.regularPolygon(50), 32.0, 0.0));
        enemyBuilder = enemyBuilder.image(this.enemyImage);
        this.gameObjects.add(enemyBuilder.build());

        final Vector2 playerPosition = new Vector2(this.w.getWidth(), this.w.getHeight()).divide(2.0);
        GameObjectBuilder terrainDetectorBuilder = new GameObjectBuilderImpl(playerPosition);
        terrainDetectorBuilder = terrainDetectorBuilder.collider(new MeshCollider(swordShape, 5.0, 0.0));
        final GameObject terrainDetector = terrainDetectorBuilder.build();
        terrainDetector.move(new Vector2(-5.0, 50.0));
        terrainDetector.setSolid(false);
        terrainDetector.rotate(Math.PI/2);
        this.gameObjects.add(terrainDetector);
        
        GameObjectBuilder playerBuilder = new PlayerBuilder(playerPosition, sword, terrainDetector, this.input, this);
        playerBuilder = playerBuilder.collider(new MeshCollider(playerShape, 20.0, 0.0));
        playerBuilder = playerBuilder.image(this.playerImage);
        this.player = playerBuilder.build();
        this.gameObjects.add(this.player);

        final List<Vector2> blockPoints = Controller.regularPolygon(4);
        for (int x = 1; x < 25; x++) {
            GameObjectBuilder blockBuilder = new BlockBuilder(new Vector2(x*GameCollisions.TILE_SIZE*2, 600));
            blockBuilder = blockBuilder.collider(new MeshCollider(blockPoints, 25.0, Math.PI/4));
            // blockBuilder = blockBuilder.image(this.blockImage);
            this.gameObjects.add(blockBuilder.build());
        }
    }

    private void addEvents() {
        this.input.addEvent("Jump", () -> this.input.isKeyPressed(KeyEvent.VK_SPACE));
        this.input.addEvent("Gravity", () -> true);
        this.input.addEvent("MoveLeft", () -> this.input.isKeyPressed(KeyEvent.VK_A));
        this.input.addEvent("MoveRight", () -> this.input.isKeyPressed(KeyEvent.VK_D));
        this.input.addEvent("MoveUp", () -> this.input.isKeyPressed(KeyEvent.VK_W));
        this.input.addEvent("MoveDown", () -> this.input.isKeyPressed(KeyEvent.VK_S));

        this.input.addEvent("SwingSword", () -> this.input.isKeyPressedOnce(KeyEvent.VK_E));
        this.input.addEvent("Shoot", () -> this.input.isKeyPressedOnce(KeyEvent.VK_Q));
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
    }

    @Override
    public List<GameObject> getGameObjects() {
        return this.gameObjects;
    }

    public void spawnProjectile() {
        GameObjectBuilder projectileBuilder = new ProjectileBuilder(this.player.getPosition(), 10.0);
        projectileBuilder = projectileBuilder.collider(new MeshCollider(this.projectileShape, 4.0, 0.0));
        projectileBuilder = projectileBuilder.image(this.projectileImage);
        this.projectiles.add(projectileBuilder.build());
    }
}
