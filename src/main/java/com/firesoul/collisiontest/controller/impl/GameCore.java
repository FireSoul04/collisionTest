package com.firesoul.collisiontest.controller.impl;

import com.firesoul.collisiontest.controller.api.GameController;
import com.firesoul.collisiontest.model.api.gameobjects.Camera;
import com.firesoul.collisiontest.controller.api.GameLogic;
import com.firesoul.collisiontest.model.impl.gameobjects.CameraImpl;
import com.firesoul.collisiontest.model.util.Vector2;
import com.firesoul.collisiontest.view.api.DrawableFactory;
import com.firesoul.collisiontest.view.api.Renderer;
import com.firesoul.collisiontest.view.impl.SwingRenderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameCore implements Runnable, GameController {

    private static final double INITIAL_SCREEN_RATIO = 2.0 / 3.0;

    private static final int WIDTH = 640;
    private static final int HEIGHT = 360;

    private final Renderer renderer;
    private final GameLogic logic;

    public GameCore() {
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        final Vector2 scale = new Vector2(screenSize.getWidth() / WIDTH, screenSize.getHeight() / HEIGHT)
            .multiply(INITIAL_SCREEN_RATIO);
        final Vector2 startPosition = new Vector2(
            (screenSize.getWidth() - WIDTH * scale.x()) / 2.0,
            (screenSize.getHeight() - HEIGHT * scale.y()) / 2.0
        );
        this.logic = new Platformer(this);

        final Camera camera = new CameraImpl(Vector2.zero(), 0.0, WIDTH, HEIGHT, this.logic.getLevel());
        this.renderer = new SwingRenderer(camera, startPosition, WIDTH, HEIGHT, scale);
    }

    @Override
    public int getWindowWidth() {
        return this.renderer.getWidth();
    }

    @Override
    public int getWindowHeight() {
        return this.renderer.getHeight();
    }

    @Override
    public int getGameWidth() {
        return this.renderer.getGameWidth();
    }

    @Override
    public int getGameHeight() {
        return this.renderer.getGameHeight();
    }

    @Override
    public InputController getInput() {
        return this.renderer.getInput();
    }

    @Override
    public Camera getCamera() {
        return this.renderer.getCamera();
    }

    @Override
    public DrawableFactory getDrawableFactory() {
        return this.renderer.getDrawableFactory();
    }

    @Override
    public void run() {
        final double period = 1000.0 / 60.0;
        double deltaTime;
        double frames = 0.0;
        long lastTime = System.currentTimeMillis();
        long frameRateStartTime = lastTime;
        while (true) {
            final long now = System.currentTimeMillis();
            deltaTime = (now - lastTime)/period;
            this.logic.update(deltaTime);
            this.renderer.update(this.logic.getLevel().getGameObjects());
            long waitTime = System.currentTimeMillis() - now;
            if (waitTime < period) {
                try {
                    Thread.sleep((long) (period - waitTime));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
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

    public static List<Vector2> regularPolygon(final int points) {
        final List<Vector2> poly = new ArrayList<>(points);
        final double angle = 2*Math.PI/points;
        for (int k = 0; k < points; k++) {
            poly.add(new Vector2(Math.cos(angle*k), Math.sin(angle*k)));
        }
        return poly;
    }
}
