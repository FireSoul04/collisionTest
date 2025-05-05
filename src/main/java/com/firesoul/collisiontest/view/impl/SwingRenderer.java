package com.firesoul.collisiontest.view.impl;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.firesoul.collisiontest.controller.impl.InputController;
import com.firesoul.collisiontest.model.api.gameobjects.Camera;
import com.firesoul.collisiontest.model.api.physics.Collider;
import com.firesoul.collisiontest.model.api.GameObject;
import com.firesoul.collisiontest.model.impl.CollisionAlgorithms;
import com.firesoul.collisiontest.model.impl.gameobjects.Player;
import com.firesoul.collisiontest.model.impl.physics.EnhancedRigidBody;
import com.firesoul.collisiontest.model.impl.physics.colliders.BoxCollider;
import com.firesoul.collisiontest.model.impl.physics.colliders.MeshCollider;
import com.firesoul.collisiontest.model.util.Vector2;
import com.firesoul.collisiontest.view.api.Drawable;
import com.firesoul.collisiontest.view.api.Renderer;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public class SwingRenderer extends JPanel implements Renderer {

    private final InputController input = new InputController();
    private final List<GameObject> gameObjects = new CopyOnWriteArrayList<>();

    private final Camera camera;
    private final int width;
    private final int height;

    private Vector2 scale;

    public SwingRenderer(final Camera camera, final Point startPosition, final int width, final int height, final Vector2 scale) {
        this.width = width;
        this.height = height;
        this.scale = scale;

        JFrame window = new JFrame("Collision test");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.addKeyListener(this.input.getKeyListener());

        window.getContentPane().add(this);
        window.getContentPane().setPreferredSize(new Dimension((int) (width * scale.x()), (int) (height * scale.y())));

        window.setLocation(startPosition);
        window.setVisible(true);
        window.pack();
        window.setMinimumSize(window.getSize());
        window.addComponentListener(new ComponentAdapter() {
            public void componentResized(final ComponentEvent e) {
            final Dimension d = ((JFrame) e.getComponent()).getContentPane().getSize();
            SwingRenderer.this.scale = new Vector2(d.getWidth() / (double) width, d.getHeight() / (double) height);
            }
        });

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
        this.gameObjects.forEach(t -> t.getSprite().ifPresent(s -> s.translate(
                s.getPosition().add(this.camera.getPosition().invert())
        )));
        this.repaint();
    }

    @Override
    public int getGameWidth() {
        return this.width;
    }

    @Override
    public int getGameHeight() {
        return this.height;
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
        g2.scale(this.scale.x(), this.scale.y());

        for (final GameObject go : this.gameObjects.stream().filter(this::isInBounds).toList()) {
            final Optional<Collider> colliderOpt = go.getCollider();
            final Optional<Drawable> spriteOpt = go.getSprite();

            if (spriteOpt.isPresent() && spriteOpt.get() instanceof SwingDrawable swingDrawable) {
                swingDrawable.drawComponent(g);
            } else if (colliderOpt.isPresent()) {
                final Collider collider = colliderOpt.get();
                boolean red = false;
                for (final Collider c : collider.getCollidedGameObjects().stream()
                        .map(GameObject::getCollider).map(Optional::orElseThrow).toList()
                ) {
                    final boolean bothSolid = collider.isSolid() && c.isSolid();
                    red |= bothSolid && collider.isCollided();
                }
                g2.setColor(red ? Color.RED : spriteOpt.isPresent() ? Color.BLACK : Color.WHITE);
//                g2.setColor(Color.WHITE);

                if (collider instanceof BoxCollider bc) {
                    g2.drawRect(
                        (int) (bc.getPosition().x() - this.camera.getPosition().x()),
                        (int) (bc.getPosition().y() - this.camera.getPosition().y()),
                        (int) bc.getWidth(),
                        (int) bc.getHeight()
                    );
                } else if (collider instanceof MeshCollider mc) {
                    final Polygon polygon = new Polygon();
                    mc.getPoints().forEach(p -> polygon.addPoint((int) p.x(), (int) p.y()));
                    g2.drawPolygon(polygon);
                }
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

            // Draw forces as arrows
            if (go instanceof Player p) {
                final List<Vector2> forces = ((EnhancedRigidBody) p.body).forcesDebug;
                g2.setColor(Color.MAGENTA);
                for (var force : forces) {
                    final Vector2 start = p.getPosition().subtract(this.camera.getPosition());
                    final Vector2 end = start.add(force.multiply(200.0));
                    final Vector2 arrowDirection = start.subtract(end).normalize().multiply(2.0);
                    final Vector2 arrow = end.add(arrowDirection);
                    g2.drawLine((int) start.x(), (int) start.y(),
                        (int) end.x(), (int) end.y());
                    g2.drawLine((int) end.x(), (int) end.y(),
                        (int) (arrow.x() + arrowDirection.y()),
                        (int) (arrow.y() + arrowDirection.x()));
                    g2.drawLine((int) end.x(), (int) end.y(),
                        (int) (arrow.x() - arrowDirection.y()),
                        (int) (arrow.y() - arrowDirection.x()));
                }
                System.out.println(((EnhancedRigidBody) p.body).forcesDebug);
                System.out.println(p.getVelocity());

                g2.setColor(Color.RED);
                final Vector2 force = p.getVelocity();
                final Vector2 start = p.getPosition().subtract(this.camera.getPosition());
                final Vector2 end = start.add(force.multiply(20.0));
                final Vector2 arrowDirection = start.subtract(end).normalize().multiply(2.0);
                final Vector2 arrow = end.add(arrowDirection);
                g2.drawLine((int) start.x(), (int) start.y(),
                        (int) end.x(), (int) end.y());
                g2.drawLine((int) end.x(), (int) end.y(),
                        (int) (arrow.x() + arrowDirection.y()),
                        (int) (arrow.y() + arrowDirection.x()));
                g2.drawLine((int) end.x(), (int) end.y(),
                        (int) (arrow.x() - arrowDirection.y()),
                        (int) (arrow.y() - arrowDirection.x()));
                forces.clear();
            }
            // DEBUG
        }
        g2.dispose();
    }

    private boolean isInBounds(final GameObject g) {
        Vector2 offset = Vector2.zero();

        if (g.getSprite().isPresent() && g.getSprite().get() instanceof SwingSprite swingSprite) {
            offset = new Vector2(swingSprite.getWidth(), swingSprite.getHeight());
        } else if (g.getCollider().isPresent()) {
            final BoxCollider box = CollisionAlgorithms.getBoxCollider(g.getCollider().get());
            offset = new Vector2(box.getWidth(), box.getHeight());
        }

        return g.getPosition().x() - offset.x() < this.camera.getPosition().x() + (this.camera.getWidth())
            && g.getPosition().y() - offset.y() < this.camera.getPosition().y() + (this.camera.getHeight())
            && g.getPosition().x() + offset.x() > this.camera.getPosition().x()
            && g.getPosition().y() + offset.y() > this.camera.getPosition().y();
    }
}
