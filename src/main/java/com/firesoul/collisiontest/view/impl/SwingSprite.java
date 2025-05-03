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

public class SwingSprite extends JComponent implements Drawable {

    private Vector2 position;
    private double orientation;

    private double direction;

    private Image sprite;
    private boolean visible;

    public SwingSprite(final String name, final Vector2 position, final double orientation, final boolean visible) {
        super();
        this.position = position;
        this.orientation = orientation;
        this.visible = visible;
        this.direction = 1.0;
        try {
            this.sprite = ImageIO.read(new File(Drawable.RESOURCES_PATH + name + ".png"));
        } catch (IOException e) {
            System.out.println("Could not load " + name + " sprite");
            System.exit(1);
        }
        this.setSize(this.sprite.getWidth(null), this.sprite.getHeight(null));
    }

    public SwingSprite(final String name, final Vector2 position, final double orientation) {
        this(name, position, orientation, true);
    }

    @Override
    public void draw() {
        this.repaint();
    }

    @Override
    public void mirrorX(double directionX) {
        this.direction = directionX;
    }

    @Override
    public void translate(final Vector2 position) {
        this.position = position;
    }

    @Override
    public void rotate(final double angle) {
        this.orientation = this.orientation + angle;
    }

    @Override
    public boolean isVisible() {
        return this.visible;
    }

    @Override
    public void setVisible(final boolean visible) {
        this.visible = visible;
    }

    public void drawSprite(final Graphics g) {
        if (this.isVisible()) {
            final Graphics2D g2 = (Graphics2D) g;
            final AffineTransform at = new AffineTransform();
            at.translate(this.position.x(), this.position.y());
            at.rotate(this.orientation);
            at.scale(this.direction, 1.0);
            at.translate(-this.sprite.getWidth(null)/2.0, -this.sprite.getHeight(null)/2.0);
            g2.drawImage(this.sprite, at, null);
        }
    }
}
