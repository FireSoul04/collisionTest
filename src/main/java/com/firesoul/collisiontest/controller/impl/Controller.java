package com.firesoul.collisiontest.controller.impl;

import com.firesoul.collisiontest.model.api.Camera;
import com.firesoul.collisiontest.model.api.Collider;
import com.firesoul.collisiontest.model.api.CollisionTest;
import com.firesoul.collisiontest.model.api.GameObject;
import com.firesoul.collisiontest.model.impl.CameraImpl;
import com.firesoul.collisiontest.model.impl.CollisionAlgorithms;
import com.firesoul.collisiontest.model.impl.CollisionAlgorithms.Swept;
import com.firesoul.collisiontest.model.impl.GameCollisions;
import com.firesoul.collisiontest.model.util.Vector2;
import com.firesoul.collisiontest.view.api.Renderer;
import com.firesoul.collisiontest.view.impl.SwingRenderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Controller implements Runnable {

    private final Renderer renderer;

    private final CollisionTest test;
    // private final CollisionTest test = new RegularPolygons(w);

    public Controller() {
        final int width = 640;
        final int height = 360;
        final Camera camera = new CameraImpl(Vector2.zero(), 0.0, width, height);

        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        final Vector2 scale = Vector2.one();//new Vector2(screenSize.getWidth() / width, screenSize.getHeight() / height).divide(2.0);
        this.renderer = new SwingRenderer(camera, width, height, scale);
        this.test = new GameCollisions(this.renderer);
        camera.setBoundsX(this.test.getWidth());
        camera.setBoundsX(this.test.getHeight());
    }

    @Override
    public void run() {
        final double period = 1000.0/60.0;
        double deltaTime = 0.0;
        double frames = 0.0;
        long lastTime = System.currentTimeMillis();
        long frameRateStartTime = lastTime;
        while (true) {
            final long now = System.currentTimeMillis();
            deltaTime = (now - lastTime)/period;
            this.update(deltaTime);
            this.render();
            long waitTime = System.currentTimeMillis() - now;
            if (waitTime < period) {
                try {
                    Thread.sleep((long) (period - waitTime));
                } catch (Exception e) {}
            }
            lastTime = now;
            frames++;
            if (System.currentTimeMillis() - frameRateStartTime >= 1000.0) {
                System.out.println(frames + " fps");
                frames = 0;
                frameRateStartTime = System.currentTimeMillis();
            }
        }
    }

    private void update(final double deltaTime) {
        this.test.readInput();
        this.checkCollisions(this.test.getGameObjects(), deltaTime);
        this.test.update(deltaTime);
    }

    private void render() {
        this.test.render();
    }

    private void checkCollisions(final List<GameObject> gameObjects, final double deltaTime) {
        final List<Collider> colliders = gameObjects.stream()
            .map(GameObject::getCollider)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .toList();
        final List<Collider> dynamicColliders = colliders.stream()
            .filter(t -> t.getAttachedGameObject().isDynamic())
            .toList();
        CollisionAlgorithms.debugRect.clear();
        CollisionAlgorithms.debugPoint.clear();
        CollisionAlgorithms.debugNormal.clear();
        for (final Collider c1 : dynamicColliders) {
            boolean collided = false;
            final Map<Collider, Double> collidersByCollisionTime = new HashMap<>();
            for (final Collider c2 : colliders.stream().filter(t -> !t.equals(c1)).toList()) {
                final Swept sw = CollisionAlgorithms.sweptAABB(c1, c2, deltaTime);
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

    public static List<Vector2> regularPolygon(final int points) {
        final List<Vector2> poly = new ArrayList<>(points);
        final double angle = 2*Math.PI/points;
        for (int k = 0; k < points; k++) {
            poly.add(new Vector2(Math.cos(angle*k), Math.sin(angle*k)));
        }
        return poly;
    }
}
