package com.firesoul.collisiontest.model.impl.gameobjects;

import com.firesoul.collisiontest.model.api.gameobjects.Camera;
import com.firesoul.collisiontest.model.impl.GameObjectImpl;
import com.firesoul.collisiontest.model.util.Vector2;

import java.util.Optional;

public class CameraImpl extends GameObjectImpl implements Camera {

    private final int width;
    private final int height;
    private double boundsX;
    private double boundsY;

    public CameraImpl(final Vector2 position, final double orientation, final int width, final int height) {
        super(position, orientation, true, Optional.empty(), Optional.empty());
        this.width = width;
        this.height = height;
        this.boundsX = width;
        this.boundsY = height;
    }

    @Override
    public void setPosition(final Vector2 position) {
        Vector2 target = position;
        if (position.x() < 0.0) {
            target = new Vector2(0.0, target.y());
        } else if (position.x() > this.boundsX) {
            target = new Vector2(this.boundsX, target.y());
        }
        if (position.y() < 0.0) {
            target = new Vector2(target.x(), 0.0);
        } else if (position.y() > this.boundsY) {
            target = new Vector2(target.x(), this.boundsY);
        }
        super.setPosition(target);
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public void setBoundsX(double boundsX) {
        this.boundsX = boundsX;
    }

    @Override
    public void setBoundsY(double boundsY) {
        this.boundsY = boundsY;
    }
}
