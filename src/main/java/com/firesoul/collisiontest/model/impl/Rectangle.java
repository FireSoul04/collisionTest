package com.firesoul.collisiontest.model.impl;

import com.firesoul.collisiontest.model.api.Drawable;
import com.firesoul.collisiontest.model.util.Vector2;

public class Rectangle implements Drawable {

    private Vector2 position;
    private int width;
    private int height;
    private boolean visible;
    private int color;

    public Rectangle(final Vector2 position, final int width, final int height, final int rgba) {
        this.position = position;
        this.width = width;
        this.height = height;
        this.visible = true;
        this.color = rgba;
    }

    @Override
    public Vector2 getPosition() {
        return this.position;
    }

    @Override
    public void mirrorX(final double directionX) {

    }

    @Override
    public double getDirectionX() {
        return 1.0;
    }

    @Override
    public void translate(final Vector2 position) {
        this.position = position;
    }

    @Override
    public void rotate(final double angle) {
        
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
    public boolean isVisible() {
        return this.visible;
    }

    @Override
    public void setVisible(final boolean visible) {
        this.visible = visible;
    }

    public int getColor() {
        return this.color;
    }
}
