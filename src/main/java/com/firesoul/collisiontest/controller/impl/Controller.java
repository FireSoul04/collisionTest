package com.firesoul.collisiontest.controller.impl;

import com.firesoul.collisiontest.model.api.Collider;
import com.firesoul.collisiontest.model.api.CollisionTest;
import com.firesoul.collisiontest.model.api.GameObject;
import com.firesoul.collisiontest.model.impl.BlockBuilder.Block;
import com.firesoul.collisiontest.model.impl.GameCollisions;
import com.firesoul.collisiontest.model.impl.RegularPolygons;
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
        final double period = (1.0/60.0)*1000;
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
        this.checkCollisions(this.test.getGameObjects());
        this.test.update(deltaTime);
    }

    private void render() {
        this.test.render();
    }

    private void checkCollisions(final List<GameObject> gameObjects) {
        final List<Collider> shapes = gameObjects.stream().map(GameObject::getCollider).filter(Optional::isPresent).map(Optional::get).toList();

        for (final Collider s1 : shapes) {
            boolean collided = false;
            final Map<Collider, Swept> shapesByCollisionTime = new HashMap<>();
            for (final Collider s2 : shapes) {
                if (!s1.equals(s2)) {
                    Swept ret = Controller.sweptAABB(s1, s2);
                    collided = ret != null;
                    // collided = Controller.SAT(s1, s2);
                    if (collided) {
                        shapesByCollisionTime.put(s2, ret);
                        s1.addCollided(s2);
                    } else {
                        s1.removeCollided(s2);
                    }
                }
            }

            for (final var e : shapesByCollisionTime.entrySet().stream().sorted((a, b) -> Double.compare(a.getValue().time(), b.getValue().time())).toList()) {
                s1.onCollide(e.getKey(), e.getValue().normal());
            }
        }
    }

    public record Rectangle(double x, double y, double w, double h) {}

    public static Rectangle fitInRect(final Collider s) {
        double minX = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;
        for (final Vector2 p : s.getPoints()) {
            minX = Math.min(minX, p.x());
            maxX = Math.max(maxX, p.x());
            minY = Math.min(minY, p.y());
            maxY = Math.max(maxY, p.y());
        }
        return new Rectangle(minX, minY, maxX - minX, maxY - minY);
    }

    public static boolean simpleAABB(final Collider s1, final Collider s2) {
        final Rectangle r1 = fitInRect(s1);
        final Rectangle r2 = fitInRect(s2);
        final boolean collided = r1.x() + r1.w() > r2.x()
            && r1.x() < r2.x() + r2.w()
            && r1.y() + r1.h() > r2.y()
            && r1.y() < r2.y() + r2.h();
        return collided;
    }

    public final record Swept(Vector2 normal, Vector2 point, double time) {
    }

    private static Swept rayAABB(final Rectangle target, final Vector2 ray, final Vector2 rayDirection) {
        Vector2 normal = Vector2.zero();
        Vector2 collisionPoint = Vector2.zero();
        Vector2 inverseDirection = new Vector2(1.0/rayDirection.x(), 1.0/rayDirection.y());
        double nearX = (target.x() - ray.x())*inverseDirection.x();
        double nearY = (target.y() - ray.y())*inverseDirection.y();
        double farX = (target.x() + target.w() - ray.x())*inverseDirection.x();
        double farY = (target.y() + target.h() - ray.y())*inverseDirection.y();

        if (nearX > farX) {
            double temp = nearX;
            nearX = farX;
            farX = temp;
        }
        if (nearY > farY) {
            double temp = nearY;
            nearY = farY;
            farY = temp;
        }
        if (nearX > farY || nearY > farX) {
            return null;
        }

        double nearHit = Math.max(nearX, nearY);
        double farHit = Math.min(farX, farY);

        if (farHit < 0.0) {
            return null;
        }

        collisionPoint = ray.add(rayDirection.multiply(nearHit));
        if (nearX > nearY) {
            normal = new Vector2(inverseDirection.x() < 0.0 ? 1.0 : -1.0, 0.0);
        } else if (nearX < nearY) {
            normal = new Vector2(0.0, inverseDirection.y() < 0.0 ? 1.0 : -1.0);
        }
        
        return new Swept(normal, collisionPoint, nearHit);
    }

    public static Swept sweptAABB(final Collider s1, final Collider s2) {
        final Rectangle r1 = fitInRect(s1);
        final Rectangle rtemp = fitInRect(s2);
        final Rectangle r2 = new Rectangle(rtemp.x() - r1.w()/2, rtemp.y() - r1.h()/2, rtemp.w() + r1.w(), rtemp.h() + r1.h());

        if (s1.getAttachedGameObject().getVelocity().equals(Vector2.zero())) {
            return null;
        }

        final Swept sw = rayAABB(r2, new Vector2(r1.x() + r1.w()/2, r1.y() + r1.h()/2), s1.getAttachedGameObject().getVelocity());
        if (sw != null && sw.time() >= 0.0 && sw.time() < 1.0) {
            return sw;
        }
        return null;
    }

    public static boolean SAT(final Collider s1, final Collider s2) {
        Collider c1 = s1;
        Collider c2 = s2;

        double overlap = Double.POSITIVE_INFINITY;
        for (int shape = 0; shape < 2; shape++) {
            if (shape == 1) {
                c1 = s2;
                c2 = s1;
            }

            for (int i = 0; i < c1.getPoints().size(); i++) {
                final Vector2 v1 = c1.getPoints().get(i);
                final Vector2 v2 = c1.getPoints().get((i + 1)%c1.getPoints().size());
                final Vector2 segment = v1.subtract(v2);
                final Vector2 normal = new Vector2(-segment.y(), segment.x());
    
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
    
				overlap = Math.min(Math.min(maxC1, maxC2) - Math.max(minC1, minC2), overlap);
                if (!(maxC2 >= minC1 && maxC1 >= minC2)) {
                    return false;
                }
            }
        }
        
        if (c1.getAttachedGameObject() instanceof Block) {
            final Vector2 d = s1.getPosition().subtract(s2.getPosition());
            final double s = d.dot(d);
            s1.move(d.multiply(overlap).divide(s));
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
