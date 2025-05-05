package com.firesoul.collisiontest.model.impl;

import com.firesoul.collisiontest.model.api.physics.Collider;
import com.firesoul.collisiontest.model.api.GameObject;
import com.firesoul.collisiontest.model.impl.physics.colliders.BoxCollider;
import com.firesoul.collisiontest.model.impl.physics.colliders.MeshCollider;
import com.firesoul.collisiontest.model.util.Vector2;

import java.util.List;

public final class CollisionAlgorithms {

    public record Collision(Vector2 normal, Vector2 point, double time) {}

    // DEBUG VISUALS
//    public static List<Rectangle> debugRect = new CopyOnWriteArrayList<>();
//    public static List<Vector2> debugPoint = new CopyOnWriteArrayList<>();
//    public static List<Rectangle> debugNormal = new CopyOnWriteArrayList<>();
    // DEBUG VISUALS

    public static Collision simpleAABB(final BoxCollider c1, final BoxCollider c2) {
        final Vector2 p1 = c1.getPosition();
        final Vector2 p2 = c2.getPosition();
        final boolean collided =
            p1.x() + c1.getWidth() > p2.x() &&
            p1.x() < p2.x() + c2.getWidth() &&
            p1.y() + c1.getHeight() > p2.y() &&
            p1.y() < p2.y() + c2.getHeight();

        if (collided) {
            Vector2 normal = Vector2.zero();
            Vector2 collisionPoint = Vector2.zero();
            Vector2 mediumPoint = new Vector2(p1.x() + p2.x(), p1.y() + p2.y()).divide(2.0);
            double collisionOnRight = p1.x() + c1.getWidth() - p2.x();
            double collisionOnLeft = p1.x() - (p2.x() + c2.getWidth());
            if (Math.abs(collisionOnRight) < 1.0) {
                normal = Vector2.right();
                collisionPoint = new Vector2(collisionOnRight, mediumPoint.y());
            } else if (Math.abs(collisionOnLeft) < 1.0) {
                normal = Vector2.left();
                collisionPoint = new Vector2(collisionOnLeft, mediumPoint.y());
            }
            double collisionOnBottom = p1.y() + c1.getHeight() - p2.y();
            double collisionOnTop = p2.y() + c2.getHeight() - p1.y();
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

            return new Collision(normal, collisionPoint, 1.0);
        }
        return null;
    }

    private static Collision rayAABB(final BoxCollider target, final Vector2 ray, final Vector2 rayDirection) {
        final Vector2 inverseDirection = new Vector2(1.0/rayDirection.x(), 1.0/rayDirection.y());
        double nearX = (target.getPosition().x() - ray.x())*inverseDirection.x();
        double nearY = (target.getPosition().y() - ray.y())*inverseDirection.y();
        double farX = (target.getPosition().x() + target.getWidth() - ray.x())*inverseDirection.x();
        double farY = (target.getPosition().y() + target.getHeight() - ray.y())*inverseDirection.y();
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
        
        return new Collision(normal, collisionPoint, nearHit);
    }

    public static Collision sweptAABB(final GameObject g1, final GameObject g2, final double deltaTime) {
        if (g1.getCollider().isEmpty() || g2.getCollider().isEmpty()) {
            return null;
        }
        final BoxCollider c1 = CollisionAlgorithms.getBoxCollider(g1.getCollider().orElseThrow());
        final BoxCollider c2 = CollisionAlgorithms.getBoxCollider(g2.getCollider().orElseThrow());
        final Vector2 p1 = c1.getPosition();
        final Vector2 p2 = c2.getPosition();
        final BoxCollider extendedRect = new BoxCollider(
                new Vector2(p2.x() - c1.getWidth()/2, p2.y() - c1.getHeight()/2),
                c2.getWidth() + c1.getWidth(), c2.getHeight() + c1.getHeight()
        );
        final Vector2 ray = new Vector2(p1.x() + c1.getWidth()/2, p1.y() + c1.getHeight()/2);
        final Collision sw = rayAABB(extendedRect, ray, g1.getVelocity().multiply(deltaTime));
        if (sw != null && sw.time() >= 0.0 && sw.time() < 1.0) {
            return sw;
        } else {
            return simpleAABB(c1, c2);
        }
    }

    public static void resolveSweptAABB(final GameObject g1, final GameObject g2, final double deltaTime) {
        final Collision sw = sweptAABB(g1, g2, deltaTime);
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

    public static boolean SAT(final GameObject g1, final GameObject g2) {
        if (g1.getCollider().isEmpty() || g2.getCollider().isEmpty()) {
            return false;
        }
        MeshCollider c1 = CollisionAlgorithms.getMeshCollider(g1.getCollider().orElseThrow());
        MeshCollider c2 = CollisionAlgorithms.getMeshCollider(g2.getCollider().orElseThrow());
        double overlap = Double.POSITIVE_INFINITY;
        for (int shape = 0; shape < 2; shape++) {
            if (shape == 1) {
                final MeshCollider temp = c1;
                c1 = c2;
                c2 = temp;
            }

            for (int i = 0; i < c1.getPoints().size(); i++) {
                final Vector2 v1 = c1.getPoints().get(i);
                final Vector2 v2 = c1.getPoints().get((i + 1) % c1.getPoints().size());
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
        
        if (g2.isStatic()) {
            final Vector2 d = g1.getPosition().subtract(g2.getPosition());
            final double s = d.dot(d);
            g1.move(d.multiply(overlap).divide(s));
        }

        return true;
    }

    public static BoxCollider getBoxCollider(final Collider collider) {;
        if (collider instanceof BoxCollider bc) {
            return bc;
        } else if (collider instanceof MeshCollider mc) {
            return CollisionAlgorithms.convertMeshToBox(mc);
        } else {
            throw new UnsupportedOperationException("Collider must be MeshCollider or BoxCollider");
        }
    }

    public static MeshCollider getMeshCollider(final Collider collider) {;
        if (collider instanceof BoxCollider bc) {
            return CollisionAlgorithms.convertBoxToMesh(bc);
        } else if (collider instanceof MeshCollider mc) {
            return mc;
        } else {
            throw new UnsupportedOperationException("Collider must be MeshCollider or BoxCollider");
        }
    }

    public static BoxCollider convertMeshToBox(final MeshCollider collider) {
        double minX = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;
        for (final Vector2 p : collider.getPoints()) {
            minX = Math.min(minX, p.x());
            maxX = Math.max(maxX, p.x());
            minY = Math.min(minY, p.y());
            maxY = Math.max(maxY, p.y());
        }
        return new BoxCollider(new Vector2(minX, minY), maxX - minX, maxY - minY);
    }

    public static MeshCollider convertBoxToMesh(final BoxCollider collider) {
        return new MeshCollider(collider.getPosition(), List.of(
                collider.getPosition(),
                collider.getPosition().add(new Vector2(collider.getWidth(), 0.0)),
                collider.getPosition().add(new Vector2(collider.getWidth(), collider.getHeight())),
                collider.getPosition().add(new Vector2(0.0, collider.getHeight()))
        ), 1.0, 0.0);
    }
}
