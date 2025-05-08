package com.firesoul.collisiontest.view.impl.renderables.shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import com.firesoul.collisiontest.view.impl.renderables.SwingRenderable;

public class SwingRectangle extends SwingRenderable {

    private final Color color;
    private int width;
    private int height;

    public SwingRectangle(final Point position, final int width, final int height, final Color color, final boolean visible) {
        super(position, visible);
        this.width = width;
        this.height = height;
        this.color = color;
    }

    @Override
    public void drawComponent(final Graphics g) {
        if (this.isVisible()) {
            final Graphics2D g2 = (Graphics2D) g;
            g2.setColor(this.color);
            g2.fillRect(this.getPosition().x, this.getPosition().y, this.width, this.height);
        }
    }

    @Override
    public void resize(final int width, final int height) {
        this.width = width;
        this.height = height;
    }
}
