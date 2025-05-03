package com.firesoul.collisiontest.model.impl;

import com.firesoul.collisiontest.model.api.Collider;
import com.firesoul.collisiontest.model.api.GameObject;
import com.firesoul.collisiontest.model.util.Vector2;

public final class CollisionAlgorithms {
    
    public record Rectangle(double x, double y, double w, double h) {}
    public record Swept(Vector2 normal, Vector2 point, double time) {}

    // DEBUG VISUALS
//    public static List<Rectangle> debugRect = new CopyOnWriteArrayList<>();
//    public static List<Vector2> debugPoint = new CopyOnWriteArrayList<>();
//    public static List<Rectangle> debugNormal = new CopyOnWriteArrayList<>();
    // DEBUG VISUALS

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

    public static Swept simpleAABB(final Rectangle r1, final Rectangle r2) {
        final boolean collided =
            r1.x() + r1.w() > r2.x() &&
            r1.x() < r2.x() + r2.w() &&
            r1.y() + r1.h() > r2.y() &&
            r1.y() < r2.y() + r2.h();

        if (collided) {
            Vector2 normal = Vector2.zero();
            Vector2 collisionPoint = Vector2.zero();
            Vector2 mediumPoint = new Vector2(r1.x() + r2.x(), r1.y() + r2.y()).divide(2.0);
            double collisionOnRight = r1.x() + r1.w() - r2.x();
            double collisionOnLeft = r1.x() - (r2.x() + r2.w());
            if (Math.abs(collisionOnRight) < 1.0) {
                normal = Vector2.right();
                collisionPoint = new Vector2(collisionOnRight, mediumPoint.y());
            } else if (Math.abs(collisionOnLeft) < 1.0) {
                normal = Vector2.left();
                collisionPoint = new Vector2(collisionOnLeft, mediumPoint.y());
            }
            double collisionOnBottom = r1.y() + r1.h() - r2.y();
            double collisionOnTop = r2.y() + r2.h() - r1.y();
            if (Math.abs(collisionOnBottom) < 1.0) {
                normal = Vector2.down();
                collisionPoint = new Vector2(mediumPoint.x(), collisionOnTop);
            } else if (Math.abs(collisionOnTop) < 1.0) {
                normal = Vector2.up();
                collisionPoint = new Vector2(mediumPoint.x(), collisionOnBottom);
            }

            // DEBUG VISUALS
//            debugRect.add(r2);
//            debugPoint.add(collisionPoint);
//            debugNormal.add(new Rectangle(collisionPoint.x(), collisionPoint.y(), normal.x(), normal.y()));
            // DEBUG VISUALS

            return new Swept(normal, collisionPoint, 1.0);
        }
        return null;
    }

    private static Swept rayAABB(final Rectangle target, final Vector2 ray, final Vector2 rayDirection) {
        final Vector2 inverseDirection = new Vector2(1.0/rayDirection.x(), 1.0/rayDirection.y());
        double nearX = (target.x() - ray.x())*inverseDirection.x();
        double nearY = (target.y() - ray.y())*inverseDirection.y();
        double farX = (target.x() + target.w() - ray.x())*inverseDirection.x();
        double farY = (target.y() + target.h() - ray.y())*inverseDirection.y();
        Vector2 normal = Vector2.zero();
        Vector2 collisionPoint;

        if (nearX > farX) {
            final double temp = nearX;
            nearX = farX;
            farX = temp;
        }
        if (nearY > farY) {
            final double temp = nearY;
            nearY = farY;
            farY = temp;
        }
        if (nearX > farY || nearY > farX) {
            return null;
        }
        final double nearHit = Math.max(nearX, nearY);
        final double farHit = Math.min(farX, farY);
        if (farHit < 0.0) {
            return null;
        }
        collisionPoint = ray.add(rayDirection.multiply(nearHit));
        
        if (Double.isNaN(collisionPoint.x()) || Double.isNaN(collisionPoint.y())) {
            return null;
        }
        if (nearX > nearY) {
            normal = new Vector2(inverseDirection.x() < 0.0 ? 1.0 : -1.0, 0.0);
        } else if (nearX < nearY) {
            normal = new Vector2(0.0, inverseDirection.y() < 0.0 ? 1.0 : -1.0);
        }

        // DEBUG VISUALS
//        debugRect.add(target);
//        debugPoint.add(collisionPoint);
//        debugNormal.add(new Rectangle(collisionPoint.x(), collisionPoint.y(), normal.x(), normal.y()));
        // DEBUG VISUALS
        
        return new Swept(normal, collisionPoint, nearHit);
    }

    public static Swept sweptAABB(final GameObject g1, final GameObject g2, final double deltaTime) {
        if (g1.getCollider().isEmpty() || g2.getCollider().isEmpty()) {
            return null;
        }

        final Collider c1 = g1.getCollider().get();
        final Collider c2 = g2.getCollider().get();

        final Rectangle r1 = fitInRect(c1);
        final Rectangle r2 = fitInRect(c2);

        if (g1.getVelocity().equals(Vector2.zero())) {
            return simpleAABB(r1, r2);
        }

        final Rectangle extendedRect = new Rectangle(r2.x() - r1.w()/2, r2.y() - r1.h()/2, r2.w() + r1.w(), r2.h() + r1.h());
        final Vector2 ray = new Vector2(r1.x() + r1.w()/2, r1.y() + r1.h()/2);
        final Swept sw = rayAABB(extendedRect, ray, g1.getVelocity().multiply(deltaTime));
        if (sw != null && sw.time() >= 0.0 && sw.time() < 1.0) {
            return sw;
        } else {
            return simpleAABB(r1, r2);
        }
    }

    public static void resolveSweptAABB(final GameObject g1, final GameObject g2, final double deltaTime) {
        final Swept sw = sweptAABB(g1, g2, deltaTime);
        if (sw != null) {
            g1.onCollision(g2, sw.normal(), sw.time());
            if (g2.isStatic()) {
                g1.setVelocity(g1.getVelocity()
                    .add(sw.normal()
                        .multiply(new Vector2(Math.abs(g1.getVelocity().x()), Math.abs(g1.getVelocity().y()))
                        .multiply(1.0 - sw.time())))
                );
            }
        }
    }

    public static boolean SAT(final Collider collider1, final Collider collider2) {
        Collider c1 = collider1;
        Collider c2 = collider2;

        double overlap = Double.POSITIVE_INFINITY;
        for (int shape = 0; shape < 2; shape++) {
            if (shape == 1) {
                c1 = collider2;
                c2 = collider1;
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
        
//        if (c2.getAttachedGameObject().isStatic()) {
//            final Vector2 d = c1.getPosition().subtract(c2.getPosition());
//            final double s = d.dot(d);
//            c1.move(d.multiply(overlap).divide(s));
//        }

        return true;
    }
}
