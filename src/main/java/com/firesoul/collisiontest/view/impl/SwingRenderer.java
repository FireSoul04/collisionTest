package com.firesoul.collisiontest.view.impl;

import javax.swing.*;

import com.firesoul.collisiontest.controller.api.EventManager;
import com.firesoul.collisiontest.controller.api.GameController;
import com.firesoul.collisiontest.controller.impl.ButtonListener;
import com.firesoul.collisiontest.controller.impl.InputListener;
import com.firesoul.collisiontest.view.api.Renderable;
import com.firesoul.collisiontest.view.api.Renderer;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class SwingRenderer implements Renderer {

    private final InputListener input = new InputListener();
    private final ButtonListener listener = new ButtonListener();

    private final SwingMainMenu menu = new SwingMainMenu(this.listener);
    private final SwingGameCanvas game = new SwingGameCanvas(this);

    private final EventManager eventManager;
    private final JPanel canvas;

    private final int width;
    private final int height;

    private double scaleX;
    private double scaleY;

    public SwingRenderer(final Point startPosition, final int width, final int height,
                         final double scaleX, final double scaleY, final GameController controller) {
        this.width = width;
        this.height = height;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.canvas = new JPanel(new CardLayout());
        this.canvas.add(this.menu, "Menu");
        this.canvas.add(this.game, "Game");
        this.canvas.addKeyListener(this.input.getKeyListener());
        this.eventManager = controller.getEventManager();
        this.eventManager.addEvent("Start", () -> this.listener.isButtonClicked("Start"));
        this.eventManager.addEvent("Exit", () -> this.listener.isButtonClicked("Exit"));

        final JFrame window = new JFrame("Collision test");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.getContentPane().add(this.canvas);
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
        this.game.add(renderable);
    }

    @Override
    public void reset() {
        this.game.reset();
    }

    @Override
    public void update() {
        final CardLayout cl = (CardLayout) this.canvas.getLayout();
        if (this.eventManager.getEvent("Start")) {
            cl.show(this.canvas, "Game");
            this.canvas.requestFocus();
        }
        this.canvas.repaint();
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
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
    public double getScaleX() {
        return this.scaleX;
    }

    @Override
    public double getScaleY() {
        return this.scaleY;
    }

    @Override
    public InputListener getInput() {
        return this.input;
    }
}
