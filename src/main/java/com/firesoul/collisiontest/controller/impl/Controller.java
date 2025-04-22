package com.firesoul.collisiontest.controller.impl;

import com.firesoul.collisiontest.model.api.Collider;
import com.firesoul.collisiontest.model.api.CollisionTest;
import com.firesoul.collisiontest.model.api.GameObject;
import com.firesoul.collisiontest.model.impl.GameCollisions;
import com.firesoul.collisiontest.model.util.Vector2;
import com.firesoul.collisiontest.view.impl.Renderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Controller implements Runnable {

    private final Renderer w = new Renderer();

    private final CollisionTest test = new GameCollisions(w);
    //private final CollisionTest test = new RegularPolygons(w);

    @Override
    public void run() {
        final double ns = 1.0E9 / 60.0;
        double deltaTime = 0.0;
        int updates = 0;
        int frames = 0;
        long lastTime = System.nanoTime();
        long frameRateStartTime = System.currentTimeMillis();
        while (true) {
            try {
                Thread.sleep(frames - (long)(frames - 100 / 60));
            } catch (InterruptedException e) {}
            final long now = System.nanoTime();
            deltaTime = deltaTime + ((now - lastTime) / ns);
            lastTime = now;
            while (deltaTime >= 1.0) {
                this.update(deltaTime);
                deltaTime--;
                updates++;
            }
            this.render();
            frames++;
            if (System.currentTimeMillis() - frameRateStartTime >= 1000.0) {
                System.out.println(updates + " ups, " + frames + " fps");
                updates = 0;
                frames = 0;
                frameRateStartTime = System.currentTimeMillis();
            }
        }
    }

    private void update(final double deltaTime) {
        this.test.update(deltaTime);
        this.checkCollisions(this.test.getGameObjects());
    }

    private void render() {
        this.test.render();
    }

    private void checkCollisions(final List<GameObject> gameObjects) {
        final List<Collider> shapes = gameObjects.stream().map(GameObject::getCollider).filter(Optional::isPresent).map(Optional::get).toList();

        for (final Collider s1 : shapes) {
            boolean collided = false;
            for (final Collider s2 : shapes) {
                if (!s1.equals(s2)) {
                    collided = Controller.SAT(s1, s2);
                    if (collided) {
                        s1.addCollided(s2);
                    } else {
                        s1.removeCollided(s2);
                    }
                }
            }
        }
    }

    public static boolean SAT(final Collider s1, final Collider s2) {
        Collider c1 = s1;
        Collider c2 = s2;

        for (int shape = 0; shape < 2; shape++) {
            if (shape == 1) {
                c1 = s2;
                c2 = s1;
            }

            for (int i = 0; i < c1.getPoints().size(); i++) {
                final Vector2 v1 = c1.getPoints().get(i);
                final Vector2 v2 = c1.getPoints().get((i + 1)%c1.getPoints().size());
                final Vector2 normal = new Vector2(-(v2.y() - v1.y()), v2.x() - v1.x());
    
                double minC1 = Double.POSITIVE_INFINITY;
                double maxC1 = Double.NEGATIVE_INFINITY;
                for (final Vector2 p : c1.getPoints()) {
                    final double dot = normal.dot(p);
                    minC1 = Math.min(minC1, dot);
                    maxC1 = Math.max(maxC1, dot);
                }
                double minC2 = Double.POSITIVE_INFINITY;
                double maxC2 = Double.NEGATIVE_INFINITY;
                for (final Vector2 p : c2.getPoints()) {
                    final double dot = normal.dot(p);
                    minC2 = Math.min(minC2, dot);
                    maxC2 = Math.max(maxC2, dot);
                }
    
                if (!(maxC2 >= minC1 && maxC1 >= minC2)) {
                    return false;
                }
            }
        }

        return true;
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
