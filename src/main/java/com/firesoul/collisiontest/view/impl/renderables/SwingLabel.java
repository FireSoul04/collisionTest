package com.firesoul.collisiontest.view.impl.renderables;

import java.awt.*;

public class SwingLabel extends SwingRenderable {

	private final Color color;
	private String text;

	public SwingLabel(final Point position, final String text, final double orientation, final int rgba, final boolean visible) {
		super(position, orientation, visible);
		this.text = text;
		this.color = new Color(rgba);
	}

	@Override
	public void drawComponent(final Graphics g) {
		if (this.isVisible()) {
			g.setColor(this.color);
			g.drawString(this.text, this.getPosition().x, this.getPosition().y);
		}
	}

	public void setText(final String text) {
		this.text = text;
	}
}
