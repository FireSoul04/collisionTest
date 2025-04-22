package com.firesoul.collisiontest.view.impl;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.firesoul.collisiontest.controller.impl.InputController;
import com.firesoul.collisiontest.model.api.Collider;
import com.firesoul.collisiontest.model.api.GameObject;
import com.firesoul.collisiontest.model.util.Vector2;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;

public class Renderer extends JPanel {

    private final JFrame window = new JFrame("Collision test");
    private final InputController input = new InputController();
    private final Map<GameObject, Polygon> polygons = new ConcurrentHashMap<>();

    public Renderer() {
        this.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.window.setSize(1280, 720);
        this.window.addKeyListener(input.getKeyListener());
        this.window.add(this);

        this.window.setVisible(true);
    }

    public void update(final List<GameObject> gameObjects) {
        this.polygons.clear();

        for (final GameObject g : gameObjects) {
            final Polygon poly = new Polygon();
            for (final Vector2 p : g.getCollider().get().getPoints()) {
                poly.addPoint((int) p.x(), (int) p.y());
            }
            this.polygons.put(g, poly);
        }
    }

    public void paintComponent(final Graphics g) {
        super.paintComponent(g);

        final Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, this.getWidth(), this.getHeight());

        for (final Map.Entry<GameObject, Polygon> e : this.polygons.entrySet()) {
            final Polygon p = e.getValue();
            final GameObject go = e.getKey();
            final Collider s = go.getCollider().get();
            final Vector2 center = s.getPosition();
            final Optional<Image> imageOpt = go.getImage();
            final Set<Collider> collidedShapes = s.getCollidedShapes();

            boolean red = false;
            for (final Collider sh : collidedShapes) {
                final boolean bothSolid = s.isSolid() && sh.isSolid();
                red |= bothSolid && s.isCollided();
            }
            g2.setColor(red ? Color.RED : imageOpt.isPresent() ? Color.BLACK : Color.WHITE);
            g2.drawPolygon(p);
            
            if (imageOpt.isPresent()) {
                final Image image = imageOpt.get();
                final AffineTransform at = new AffineTransform();
                at.translate(center.x(), center.y());
                at.rotate(s.getOrientation());
                at.translate(-image.getWidth(this), -image.getHeight(this));
                at.scale(2.0, 2.0);
                g2.drawImage(image, at, this);
            } else {
                g2.drawLine((int) center.x(), (int) center.y(), p.xpoints[0], p.ypoints[0]);
            }
        }
        g2.dispose();
    }

    public InputController getInput() {
        return this.input;
    }
}
