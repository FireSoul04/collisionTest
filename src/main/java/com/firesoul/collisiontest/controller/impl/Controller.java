package com.firesoul.collisiontest.controller.impl;

import com.firesoul.collisiontest.model.api.gameobjects.Camera;
import com.firesoul.collisiontest.model.api.Collider;
import com.firesoul.collisiontest.model.api.CollisionTest;
import com.firesoul.collisiontest.model.api.GameObject;
import com.firesoul.collisiontest.model.impl.gameobjects.CameraImpl;
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

    private static final double INITIAL_SCREEN_RATIO = 2.0 / 3.0;

    private final Renderer renderer;

    private final CollisionTest test;
    // private final CollisionTest test = new RegularPolygons(w);

    public Controller() {
        final int width = 640;
        final int height = 360;
        final Camera camera = new CameraImpl(Vector2.zero(), 0.0, width, height);

        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        final Vector2 scale = new Vector2(screenSize.getWidth() / width, screenSize.getHeight() / height)
            .multiply(INITIAL_SCREEN_RATIO);
        final Point startPosition = new Point(
            (int) ((screenSize.getWidth() - width * scale.x()) / 2),
            (int) ((screenSize.getHeight() - height * scale.y()) / 2)
        );
        this.renderer = new SwingRenderer(camera, startPosition, width, height, scale);
        this.test = new GameCollisions(this.renderer);
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
        this.test.update(deltaTime);
    }

    private void render() {
        this.test.render();
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
