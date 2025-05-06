package com.firesoul.collisiontest.view.impl;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class SwingSprite extends SwingRenderable {

    private Image sprite;

    public SwingSprite(final Image sprite, final Point position, final double orientation, final boolean visible) {
        super(position, orientation, visible);
        this.sprite = sprite;
        this.setSize(this.sprite.getWidth(null), this.sprite.getHeight(null));
    }

    public SwingSprite(final Image sprite) {
        this(sprite, new Point(0, 0), 0.0, true);
    }

    @Override
    public void drawComponent(final Graphics g) {
        if (this.isVisible()) {
            final Graphics2D g2 = (Graphics2D) g;
            final AffineTransform at = new AffineTransform();
            at.translate(this.getPosition().x, this.getPosition().y);
            at.rotate(this.getOrientation());
            at.scale(this.getDirection(), 1.0);
            at.translate(-this.sprite.getWidth(null)/2.0, -this.sprite.getHeight(null)/2.0);
            g2.drawImage(this.sprite, at, null);
        }
    }
}
