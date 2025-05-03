package com.firesoul.collisiontest.model.impl.gameobjects.colliders;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.firesoul.collisiontest.model.util.Vector2;

public class MeshCollider extends AbstractCollider {

    private final List<Vector2> points = new CopyOnWriteArrayList<>();

    public MeshCollider(final Vector2 position, final List<Vector2> points, final double size, final double angle) {
        super(position);
        this.points.addAll(points.stream().map(t -> t.multiply(size)).toList());
        this.rotate(angle);
    }

    public MeshCollider(final Vector2 position, final List<Vector2> points, final double size) {
        this(position, points, size, 0.0);
    }

    @Override
    public void rotate(final double angle) {
        super.rotate(angle);
        this.points.replaceAll(p -> p.rotate(angle));
    }

    public List<Vector2> getPoints() {
        return this.points.stream().map(p -> p.add(this.getPosition())).toList();
    }
}
