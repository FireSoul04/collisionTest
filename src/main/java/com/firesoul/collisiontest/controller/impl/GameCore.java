package com.firesoul.collisiontest.controller.impl;

import com.firesoul.collisiontest.controller.api.EventManager;
import com.firesoul.collisiontest.controller.api.loader.DrawableLoader;
import com.firesoul.collisiontest.controller.api.GameController;
import com.firesoul.collisiontest.controller.impl.loader.DrawableLoaderImpl;
import com.firesoul.collisiontest.model.api.GameObject;
import com.firesoul.collisiontest.model.api.gameobjects.Camera;
import com.firesoul.collisiontest.controller.api.GameLogic;
import com.firesoul.collisiontest.model.impl.CollisionAlgorithms;
import com.firesoul.collisiontest.model.impl.CameraImpl;
import com.firesoul.collisiontest.model.impl.physics.colliders.BoxCollider;
import com.firesoul.collisiontest.model.util.Vector2;
import com.firesoul.collisiontest.view.api.Renderer;
import com.firesoul.collisiontest.view.impl.SwingRenderer;
import com.firesoul.collisiontest.view.impl.renderables.SwingSprite;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameCore implements Runnable, GameController {

    private static final double INITIAL_SCREEN_RATIO = 2.0 / 3.0;

    private static final int WIDTH = 640;
    private static final int HEIGHT = 360;

    private final DrawableLoader dl = new DrawableLoaderImpl();
    private final EventManager eventManager = new EventManagerImpl();
    private final Renderer renderer;
    private final GameLogic logic;
    private final Camera camera;

    public GameCore() {
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        final Vector2 scale = new Vector2(screenSize.getWidth() / WIDTH, screenSize.getHeight() / HEIGHT)
            .multiply(INITIAL_SCREEN_RATIO);
        final Point startPosition = new Point(
            (int) (screenSize.getWidth() - WIDTH * scale.x()) / 2,
            (int) (screenSize.getHeight() - HEIGHT * scale.y()) / 2
        );
        this.renderer = new SwingRenderer(startPosition, WIDTH, HEIGHT, scale.x(), scale.y());
        this.logic = new Platformer(this);
        this.camera = new CameraImpl(Vector2.zero(), 0.0, WIDTH, HEIGHT, this.logic.getLevel());
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
    public EventManager getEventManager() { return this.eventManager; }

    @Override
    public Camera getCamera() {
        return this.camera;
    }

    @Override
    public DrawableLoader getDrawableLoader() {
        return this.dl;
    }

    @Override
    public void run() {
        final double period = 1000.0 / 60.0;
        double deltaTime;
        double frames = 0.0;
        long lastTime = System.currentTimeMillis();
        long frameRateStartTime = lastTime;
        while (true) {//piroddi dice spaghetti code
            final long now = System.currentTimeMillis();
            deltaTime = (now - lastTime)/period;
            this.logic.update(deltaTime);
            this.render();
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

    public void render() {
        final List<GameObject> gameObjects = this.logic.getLevel().getGameObjects();

        this.dl.update();
        this.renderer.reset();
        gameObjects.stream().filter(this::isInBounds).forEach(t -> {
            t.getSprite().ifPresent(d -> this.renderer.add(this.dl.getFromDrawable(d, this.camera)));
        });
        this.renderer.update();


//        for (final GameObject go : gameObjects.stream().filter(this::isInBounds).toList()) {
//            final Optional<Collider> colliderOpt = go.getCollider();
//            final Optional<Drawable> spriteOpt = go.getSprite();
//
//            if (spriteOpt.isPresent() && spriteOpt.get() instanceof SwingDrawable swingDrawable) {
//               this.renderer.add(swingDrawable);
//            } else if (colliderOpt.isPresent()) {
//                final Collider collider = colliderOpt.get();
//                boolean red = false;
//                for (final Collider c : collider.getCollidedGameObjects().stream()
//                    .map(GameObject::getCollider).map(Optional::orElseThrow).toList()
//                ) {
//                    final boolean bothSolid = collider.isSolid() && c.isSolid();
//                    red |= bothSolid && collider.isCollided();
//                }
//                g2.setColor(red ? Color.RED : spriteOpt.isPresent() ? Color.BLACK : Color.WHITE);
////                g2.setColor(Color.WHITE);
//
//                if (collider instanceof BoxCollider bc) {
//                    g2.drawRect(
//                        (int) (bc.getPosition().x() - this.camera.getPosition().x()),
//                        (int) (bc.getPosition().y() - this.camera.getPosition().y()),
//                        (int) bc.getWidth(),
//                        (int) bc.getHeight()
//                    );
//                } else if (collider instanceof MeshCollider mc) {
//                    final Polygon polygon = new Polygon();
//                    mc.getPoints().forEach(p -> polygon.addPoint((int) p.x(), (int) p.y()));
//                    g2.drawPolygon(polygon);
//                }
//            }

//            debug(g2, go);
//        }
    }

    private boolean isInBounds(final GameObject g) {
        Vector2 offset;
        if (g.getSprite().isPresent() && g.getSprite().get() instanceof SwingSprite swingSprite) {
            offset = new Vector2(swingSprite.getWidth(), swingSprite.getHeight());
        } else if (g.getCollider().isPresent()) {
            final BoxCollider box = CollisionAlgorithms.getBoxCollider(g.getCollider().get());
            offset = new Vector2(box.getWidth(), box.getHeight());
        } else {
            return true;
        }
        return g.getPosition().x() - offset.x() < this.camera.getPosition().x() + (this.camera.getWidth())
            && g.getPosition().y() - offset.y() < this.camera.getPosition().y() + (this.camera.getHeight())
            && g.getPosition().x() + offset.x() > this.camera.getPosition().x()
            && g.getPosition().y() + offset.y() > this.camera.getPosition().y();
    }

//    private void debug(final Graphics2D g2, final GameObject go) {
//        if (go instanceof Player p) {
//            final List<Vector2> forces = ((EnhancedRigidBody) p.body).forcesDebug;
//            g2.setColor(Color.MAGENTA);
//            for (var force : forces) {
//                final Vector2 start = p.getPosition().subtract(this.camera.getPosition());
//                final Vector2 end = start.add(force.multiply(200.0));
//                final Vector2 arrowDirection = start.subtract(end).normalize().multiply(2.0);
//                final Vector2 arrow = end.add(arrowDirection);
//                g2.drawLine((int) start.x(), (int) start.y(),
//                    (int) end.x(), (int) end.y());
//                g2.drawLine((int) end.x(), (int) end.y(),
//                    (int) (arrow.x() + arrowDirection.y()),
//                    (int) (arrow.y() + arrowDirection.x()));
//                g2.drawLine((int) end.x(), (int) end.y(),
//                    (int) (arrow.x() - arrowDirection.y()),
//                    (int) (arrow.y() - arrowDirection.x()));
//            }
//
//            g2.setColor(Color.RED);
//            final Vector2 force = p.getVelocity();
//            final Vector2 start = p.getPosition().subtract(this.camera.getPosition());
//            final Vector2 end = start.add(force.multiply(20.0));
//            final Vector2 arrowDirection = start.subtract(end).normalize().multiply(2.0);
//            final Vector2 arrow = end.add(arrowDirection);
//            g2.drawLine((int) start.x(), (int) start.y(),
//                (int) end.x(), (int) end.y());
//            g2.drawLine((int) end.x(), (int) end.y(),
//                (int) (arrow.x() + arrowDirection.y()),
//                (int) (arrow.y() + arrowDirection.x()));
//            g2.drawLine((int) end.x(), (int) end.y(),
//                (int) (arrow.x() - arrowDirection.y()),
//                (int) (arrow.y() - arrowDirection.x()));
//            forces.clear();
//        }
//    }

    public static List<Vector2> regularPolygon(final int points) {
        final List<Vector2> poly = new ArrayList<>(points);
        final double angle = 2*Math.PI/points;
        for (int k = 0; k < points; k++) {
            poly.add(new Vector2(Math.cos(angle*k), Math.sin(angle*k)));
        }
        return poly;
    }
}
