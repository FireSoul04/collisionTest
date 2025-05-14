package com.firesoul.collisiontest.view.impl.renderables;

import java.awt.*;

public class SwingLabel extends SwingRenderable {

	private final String text;

	public SwingLabel(final Point position, final String text, final double orientation, final boolean visible) {
		super(position, orientation, visible);
		this.text = text;
	}

	@Override
	public void drawComponent(final Graphics g) {
		if (this.isVisible()) {
			g.drawString(this.text, this.getPosition().x, this.getPosition().y);
		}
	}
}
