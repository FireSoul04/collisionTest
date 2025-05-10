package com.firesoul.collisiontest.view.impl;

import com.firesoul.collisiontest.view.api.Renderable;
import com.firesoul.collisiontest.view.api.Renderer;
import com.firesoul.collisiontest.view.impl.renderables.SwingRenderable;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SwingGameCanvas extends JPanel {

	private final Renderer renderer;
	private final List<SwingRenderable> drawables = new CopyOnWriteArrayList<>();

	public SwingGameCanvas(final Renderer renderer) {
		this.renderer = renderer;
	}

	public void add(final Renderable renderable) {
		if (renderable instanceof SwingRenderable swingRenderable) {
			this.drawables.add(swingRenderable);
		} else {
			throw new IllegalArgumentException("Invalid type of renderable for Swing view");
		}
	}

	public void reset() {
		this.drawables.clear();
	}

	public void update() {
		this.repaint();
	}

	@Override
	protected void paintComponent(final Graphics g) {
		super.paintComponent(g);

		final Graphics2D g2 = (Graphics2D) g;
		g2.fillRect(0, 0, this.getWidth(), this.getHeight());
		g2.scale(this.renderer.getScaleX(), this.renderer.getScaleY());

		this.drawables.forEach(t -> t.drawComponent(g2));

		g2.dispose();
	}
}
