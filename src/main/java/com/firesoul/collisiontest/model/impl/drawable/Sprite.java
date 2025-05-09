package com.firesoul.collisiontest.model.impl.drawable;

import com.firesoul.collisiontest.model.api.drawable.Drawable;
import com.firesoul.collisiontest.model.util.Vector2;

public class Sprite implements Drawable {

	private final int width;
	private final int height;

	private Vector2 position = Vector2.zero();
	private double orientation = 0.0;
	private double directionX = 1.0;
	private boolean visible = true;

	public Sprite(final int width, final int height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public Vector2 getPosition() {
		return this.position;
	}

	@Override
	public void mirrorX(final double directionX) {
		this.directionX = directionX;
	}

	@Override
	public double getDirectionX() {
		return this.directionX;
	}

	@Override
	public void translate(final Vector2 position) {
		this.position = position;
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
