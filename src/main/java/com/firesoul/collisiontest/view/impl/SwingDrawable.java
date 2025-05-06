package com.firesoul.collisiontest.view.impl;

import com.firesoul.collisiontest.model.util.Vector2;
import com.firesoul.collisiontest.view.api.Drawable;

import javax.swing.*;
import java.awt.*;

public abstract class SwingDrawable extends JComponent implements Drawable {

    private Vector2 position;
    private Vector2 scale;
    private double orientation;
    private double direction;
    private boolean visible;

    public SwingDrawable(final Vector2 position, final double orientation, final boolean visible) {
        super();
        this.position = position;
        this.orientation = orientation;
        this.scale = Vector2.one();
        this.visible = visible;
        this.direction = 1.0;
    }

    public SwingDrawable(final Vector2 position, final boolean visible) {
        this(position, 0.0, visible);
    }

    @Override
    public void mirrorX(double directionX) {
        this.direction = directionX;
    }

    @Override
    public void scale(final Vector2 scale) { this.scale = scale; }

    @Override
    public void translate(final Vector2 position) {
        this.position = position;
    }

    @Override
    public void rotate(final double angle) {
        this.orientation = this.orientation + angle;
    }

    @Override
    public void draw() {

    }

    @Override
    public boolean isVisible() {
        return this.visible;
    }

    @Override
    public void setVisible(final boolean visible) {
        this.visible = visible;
    }

    @Override
    public Vector2 getPosition() {
        return this.position;
    }

    protected double getOrientation() {
        return this.orientation;
    }

    protected double getDirection() {
        return this.direction;
    }

    protected Vector2 getScale() {
        return this.scale;
    }

    abstract public void drawComponent(final Graphics g);
}
