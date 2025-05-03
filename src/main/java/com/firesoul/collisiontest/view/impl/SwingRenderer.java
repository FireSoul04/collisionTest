package com.firesoul.collisiontest.view.impl;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.firesoul.collisiontest.controller.impl.InputController;
import com.firesoul.collisiontest.model.api.Collider;
import com.firesoul.collisiontest.model.api.GameObject;
import com.firesoul.collisiontest.model.impl.CollisionAlgorithms;
import com.firesoul.collisiontest.model.impl.CollisionAlgorithms.Rectangle;
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

    public SwingRenderer() {
        this.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.window.setSize(1280, 720);
        this.window.addKeyListener(this.input.getKeyListener());
        this.window.add(this);

        this.window.setVisible(true);
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

        for (final GameObject g : gameObjects) {
            this.gameObjects.add(g);
        }
        this.repaint();
    }

    @Override
    public InputController getInput() {
        return this.input;
    }

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);

        final Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, this.getWidth(), this.getHeight());

        for (final GameObject go : this.gameObjects) {
            final Optional<Collider> colliderOpt = go.getCollider();
            final Optional<Drawable> spriteOpt = go.getSprite();

            if (colliderOpt.isPresent()) {
                final Collider collider = colliderOpt.get();
                final Set<Collider> collidedShapes = collider.getCollidedShapes();

                boolean red = false;
                for (final Collider sh : collidedShapes) {
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
}
