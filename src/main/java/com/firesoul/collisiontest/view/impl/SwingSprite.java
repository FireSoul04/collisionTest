package com.firesoul.collisiontest.view.impl;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import com.firesoul.collisiontest.model.util.Vector2;
import com.firesoul.collisiontest.view.api.Drawable;
import com.firesoul.collisiontest.view.api.Renderer;

public class SwingSprite extends JComponent implements Drawable {

    private Vector2 position;
    private double orientation;

    private Image sprite;

    public SwingSprite(final String name, final Vector2 position, final double orientation, final Renderer renderer) {
        super();
        this.position = position;
        this.orientation = orientation;
        try {
            this.sprite = ImageIO.read(new File(Drawable.RESOURCES_PATH + name + ".png"));
        } catch (IOException e) {
            System.out.println("Could not load " + name + " sprite");
            System.exit(1);
        }
        this.setSize(this.sprite.getWidth(null), this.sprite.getHeight(null));
        renderer.add(this);
    }

    @Override
    public void draw() {
        this.repaint();
    }

    @Override
    public void translate(final Vector2 position) {
        this.position = position;
    }

    @Override
    public void rotate(final double angle) {
        this.orientation = this.orientation + angle;
    }

    public void drawSprite(final Graphics g) {
        final Graphics2D g2 = (Graphics2D) g;
        final AffineTransform at = new AffineTransform();
        at.translate(this.position.x(), this.position.y());
        at.rotate(this.orientation);
        at.translate(-this.sprite.getWidth(this), -this.sprite.getHeight(this));
        at.scale(2.0, 2.0);
        g2.drawImage(this.sprite, at, this);
    }
}
