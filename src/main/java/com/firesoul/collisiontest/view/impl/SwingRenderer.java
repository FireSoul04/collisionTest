package com.firesoul.collisiontest.view.impl;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.firesoul.collisiontest.controller.impl.InputController;
import com.firesoul.collisiontest.view.api.Renderable;
import com.firesoul.collisiontest.view.api.Renderer;
import com.firesoul.collisiontest.view.impl.renderables.SwingRenderable;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class SwingRenderer extends JPanel implements Renderer {

    private final InputController input = new InputController();
    private final List<SwingRenderable> drawables = new ArrayList<>();
    private final Queue<SwingRenderable> drawablesQ = new ArrayDeque<>();

    private final int width;
    private final int height;

    private double scaleX;
    private double scaleY;

    public SwingRenderer(final Point startPosition, final int width, final int height, final double scaleX, final double scaleY) {
        this.width = width;
        this.height = height;
        this.scaleX = scaleX;
        this.scaleY = scaleY;

        final JFrame window = new JFrame("Collision test");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.addKeyListener(this.input.getKeyListener());

        window.getContentPane().add(this);
        window.getContentPane().setPreferredSize(new Dimension((int) (width * scaleX), (int) (height * scaleY)));

        window.setLocation(startPosition);
        window.setVisible(true);
        window.pack();
        window.setMinimumSize(window.getSize());
        window.addComponentListener(new ComponentAdapter() {
            public void componentResized(final ComponentEvent e) {
            final Dimension d = ((JFrame) e.getComponent()).getContentPane().getSize();
            SwingRenderer.this.scaleX = d.getWidth() / (double) width;
            SwingRenderer.this.scaleY = d.getHeight() / (double) height;
            }
        });
    }

    @Override
    public void add(final Renderable renderable) {
        if (renderable instanceof SwingRenderable swingRenderable) {
            this.drawablesQ.add(swingRenderable);
        } else {
            throw new IllegalArgumentException("Invalid type of renderable for Swing view");
        }
    }

    @Override
    public void reset() {
        this.drawables.clear();
    }

    @Override
    public void update() {
        this.drawables.addAll(this.drawablesQ);
        this.drawablesQ.clear();
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
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);

        final Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(0, 0, this.getWidth(), this.getHeight());
        g2.scale(this.scaleX, this.scaleY);

        SwingUtilities.invokeLater(() -> this.drawables.forEach(t -> t.drawComponent(g)));

        g2.dispose();
    }
}
