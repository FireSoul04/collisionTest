package com.firesoul.collisiontest.model.impl;

import com.firesoul.collisiontest.model.api.UI;
import com.firesoul.collisiontest.model.util.Vector2;

public class UIBase implements UI {

	private final int width;
	private final int height;
	private final Vector2 position;

	private double orientation = 0.0;
	private boolean visible;

	public UIBase(final Vector2 position, final int width, final int height) {
		this.position = position;
		this.width = width;
		this.height = height;
		this.visible = true;
	}

	@Override
	public Vector2 getPosition() {
		return this.position;
	}

	@Override
	public void mirrorX(final double directionX) {

	}

	@Override
	public double getDirectionX() {
		return 1.0;
	}

	@Override
	public void translate(final Vector2 position) {

	}

	@Override
	public void rotate(final double angle) {
		this.orientation = this.orientation + angle;
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
	public boolean isVisible() {
		return this.visible;
	}

	@Override
	public void setVisible(final boolean visible) {
		this.visible = visible;
	}
}
