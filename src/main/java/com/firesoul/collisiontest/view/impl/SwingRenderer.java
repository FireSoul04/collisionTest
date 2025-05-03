package com.firesoul.collisiontest.view.impl;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.firesoul.collisiontest.controller.impl.InputController;
import com.firesoul.collisiontest.model.api.Camera;
import com.firesoul.collisiontest.model.api.Collider;
import com.firesoul.collisiontest.model.api.GameObject;
import com.firesoul.collisiontest.model.impl.CameraImpl;
import com.firesoul.collisiontest.model.impl.CollisionAlgorithms;
import com.firesoul.collisiontest.model.impl.CollisionAlgorithms.Rectangle;
import com.firesoul.collisiontest.model.util.Vector2;
import com.firesoul.collisiontest.view.api.Drawable;
import com.firesoul.collisiontest.view.api.Renderer;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class SwingRenderer extends JPanel implements Renderer {

    private final JFrame window = new JFrame("Collision test");
    private final InputController input = new InputController();
    private final List<GameObject> gameObjects = new CopyOnWriteArrayList<>();

    private final Camera camera;

    public SwingRenderer(final Camera camera, final int width, final int height) {
        this.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.window.setSize(width, height);
        this.window.addKeyListener(this.input.getKeyListener());
        this.window.add(this);

        this.window.setVisible(true);

        this.camera = camera;
    }

    @Override
    public void add(final Drawable drawable) {
        if (drawable instanceof SwingSprite swingSprite) {
            super.add(swingSprite);
        }
    }

    @Override
    public void update(final List<GameObject> gameObjects) {
        this.gameObjects.clear();
        this.gameObjects.addAll(gameObjects);
        this.repaint();
    }

    @Override
    public InputController getInput() {
        return this.input;
    }

    @Override
    public Camera getCamera() {
        return this.camera;
    }

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);

        final Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, this.getWidth(), this.getHeight());
        g2.translate(-this.camera.getPosition().x(), -this.camera.getPosition().y());

        for (final GameObject go : this.gameObjects.stream().filter(this::isInBounds).toList()) {
            final Optional<Collider> colliderOpt = go.getCollider();
            final Optional<Drawable> spriteOpt = go.getSprite();

            if (colliderOpt.isPresent()) {
                final Collider collider = colliderOpt.get();
                boolean red = false;
                for (final Collider sh : collider.getCollidedShapes()) {
                    final boolean bothSolid = collider.isSolid() && sh.isSolid();
                    red |= bothSolid && collider.isCollided();
                }
                g2.setColor(red ? Color.RED : spriteOpt.isPresent() ? Color.BLACK : Color.WHITE);
                final Rectangle hitbox = CollisionAlgorithms.fitInRect(collider);
                g2.drawRect((int) hitbox.x(), (int) hitbox.y(), (int) hitbox.w(), (int) hitbox.h());
            }
            
            if (spriteOpt.isPresent() && spriteOpt.get() instanceof SwingSprite swingSprite) {
                swingSprite.drawSprite(g);
            }

            // DEBUG
            // g2.setColor(Color.MAGENTA);
            // for (var x : CollisionAlgorithms.debugRect) {
            //     g2.drawRect((int) x.x(), (int) x.y(), (int) x.w(), (int) x.h());
            // }
            // g2.setColor(Color.CYAN);
            // for (var x : CollisionAlgorithms.debugPoint) {
            //     g2.fillOval((int) x.x()-5, (int) x.y()-5, 10, 10);
            // }
            // g2.setColor(Color.ORANGE);
            // for (var x : CollisionAlgorithms.debugNormal) {
            //     g2.drawRect((int) x.x(), (int) x.y(), (int) x.w()*100, (int) x.h()*100);
            // }
            // DEBUG
        }
        g2.dispose();
    }

    private boolean isInBounds(final GameObject g) {
        Vector2 offset = Vector2.zero();

        if (g.getSprite().isPresent() && g.getSprite().get() instanceof SwingSprite swingSprite) {
            offset = new Vector2(swingSprite.getWidth(), swingSprite.getHeight());
        } else if (g.getCollider().isPresent()) {
            final var rect = CollisionAlgorithms.fitInRect(g.getCollider().get());
            offset = new Vector2(rect.w(), rect.h());
        }

        return g.getPosition().x() - offset.x() < this.camera.getPosition().x() + (this.camera.getWidth())
            && g.getPosition().y() - offset.y() < this.camera.getPosition().y() + (this.camera.getHeight())
            && g.getPosition().x() + offset.x() > this.camera.getPosition().x()
            && g.getPosition().y() + offset.y() > this.camera.getPosition().y();
    }
}
