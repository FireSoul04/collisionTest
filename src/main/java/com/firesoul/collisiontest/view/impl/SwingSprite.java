package com.firesoul.collisiontest.view.impl;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.firesoul.collisiontest.model.util.Vector2;
import com.firesoul.collisiontest.view.api.Drawable;

public class SwingSprite extends SwingDrawable {

    private Image sprite;

    public SwingSprite(final String name, final Vector2 position, final double orientation, final boolean visible) {
        super(position, orientation, visible);
        try {
            this.sprite = ImageIO.read(new File(Drawable.RESOURCES_PATH + name + ".png"));
        } catch (IOException e) {
            System.out.println("Could not load " + name + " sprite");
            System.exit(1);
        }
        this.setSize(this.sprite.getWidth(null), this.sprite.getHeight(null));
    }

    public SwingSprite(final String name, final Vector2 position) {
        this(name, position, 0.0, true);
    }

    @Override
    public void drawComponent(final Graphics g) {
        if (this.isVisible()) {
            final Graphics2D g2 = (Graphics2D) g;
            final AffineTransform at = new AffineTransform();
            at.translate(this.getPosition().x(), this.getPosition().y());
            at.rotate(this.getOrientation());
            at.scale(this.getDirection(), 1.0);
            at.translate(-this.sprite.getWidth(null)/2.0, -this.sprite.getHeight(null)/2.0);
            g2.drawImage(this.sprite, at, null);
        }
    }
}
