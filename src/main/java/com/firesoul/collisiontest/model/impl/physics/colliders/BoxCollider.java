package com.firesoul.collisiontest.model.impl.physics.colliders;

import com.firesoul.collisiontest.model.util.Vector2;

public class BoxCollider extends AbstractCollider {

    private final double width;
    private final double height;

    public BoxCollider(final Vector2 position, final double width, final double height) {
        super(position);
        this.width = width;
        this.height = height;
    }

    @Override
    public void setPosition(final Vector2 position) {
        super.setPosition(position.subtract(new Vector2(this.width, this.height).divide(2.0)));
    }

    @Override
    public void rotate(final double angle) {

    }

    public double getWidth() {
        return this.width;
    }

    public double getHeight() {
        return this.height;
    }
}
