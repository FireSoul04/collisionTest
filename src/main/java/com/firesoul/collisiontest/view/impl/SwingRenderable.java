package com.firesoul.collisiontest.view.impl;

import com.firesoul.collisiontest.view.api.Renderable;

import javax.swing.*;
import java.awt.*;

public abstract class SwingRenderable extends JComponent implements Renderable {

    private Point position;
    private double scaleX;
    private double scaleY;
    private double orientation;
    private double direction;
    private boolean visible;

    public SwingRenderable(final Point position, final double orientation, final boolean visible) {
        super();
        this.position = position;
        this.orientation = orientation;
        this.scaleX = 1.0;
        this.scaleY = 1.0;
        this.visible = visible;
        this.direction = 1.0;
    }

    public SwingRenderable(final Point position, final boolean visible) {
        this(position, 0.0, visible);
    }

    @Override
    public void mirrorX(double directionX) {
        this.direction = directionX;
    }

    @Override
    public void scale(final double scaleX, final double scaleY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    @Override
    public void translate(final Point position) {
        this.position = position;
    }

    @Override
    public void translate(final double x, final double y) {
        this.position = new Point((int) x, (int) y);
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
    public Point getPosition() {
        return this.position;
    }

    protected double getOrientation() {
        return this.orientation;
    }

    protected double getDirection() {
        return this.direction;
    }

    protected double getScaleX() {
        return this.scaleX;
    }

    protected double getScaleY() {
        return this.scaleY;
    }

    abstract public void drawComponent(final Graphics g);
}
