package com.firesoul.collisiontest.controller.impl;

import com.firesoul.collisiontest.model.api.Collider;
import com.firesoul.collisiontest.model.api.CollisionTest;
import com.firesoul.collisiontest.model.api.GameObject;
import com.firesoul.collisiontest.model.impl.CollisionAlgorithms;
import com.firesoul.collisiontest.model.impl.CollisionAlgorithms.Swept;
import com.firesoul.collisiontest.model.impl.GameCollisions;
import com.firesoul.collisiontest.model.util.Vector2;
import com.firesoul.collisiontest.view.impl.Renderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Controller implements Runnable {

    private final Renderer w = new Renderer();

    private final CollisionTest test = new GameCollisions(w);
    // private final CollisionTest test = new RegularPolygons(w);

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
        for (final Collider s1 : dynamicColliders) {
            boolean collided = false;
            final Map<Collider, Double> collidersByCollisionTime = new HashMap<>();
            for (final Collider s2 : colliders.stream().filter(t -> !t.equals(s1)).toList()) {
                Swept ret = CollisionAlgorithms.sweptAABB(s1, s2, deltaTime);
                collided = ret != null;
                // collided = Controller.SAT(s1, s2);
                if (collided) {
                    collidersByCollisionTime.put(s2, ret.time());
                    s1.addCollided(s2);
                } else {
                    s1.removeCollided(s2);
                }
            }

            for (var x : collidersByCollisionTime.entrySet().stream().sorted((a, b) -> Double.compare(a.getValue(), b.getValue())).toList()) {
                CollisionAlgorithms.resolveSweptAABB(s1, x.getKey(), deltaTime);
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
