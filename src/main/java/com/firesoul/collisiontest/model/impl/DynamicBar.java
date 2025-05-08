package com.firesoul.collisiontest.model.impl;

import com.firesoul.collisiontest.model.api.Bar;
import com.firesoul.collisiontest.model.util.Vector2;

public class DynamicBar implements Bar {

	private final int width;
	private final int height;

	private Vector2 position;
	private double percentage;
	private boolean visible;

	public DynamicBar(final int width, final int height) {
		this.width = width;
		this.height = height;
		this.position = Vector2.zero();
		this.percentage = 1.0;
	}

	@Override
	public double getCurrentPercentage() {
		return this.percentage;
	}

	@Override
	public void setCurrentPercentage(final double percentage) {
		this.percentage = percentage;
	}

	@Override
	public Vector2 getPosition() {
		return this.position;
	}

	@Override
	public void translate(final Vector2 position) {
		this.position = position;
	}

	@Override
	public void mirrorX(final double directionX) {
		
	}

	@Override
	public double getDirectionX() {
		return 1.0;
	}

	@Override
	public void rotate(final double angle) {
		
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
