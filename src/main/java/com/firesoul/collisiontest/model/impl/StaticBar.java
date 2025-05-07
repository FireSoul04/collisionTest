package com.firesoul.collisiontest.model.impl;

import com.firesoul.collisiontest.model.api.Bar;
import com.firesoul.collisiontest.model.util.Vector2;

public class StaticBar extends UIBase implements Bar {

	private double percentage;

	public StaticBar(final Vector2 position, final int width, final int height) {
		super(position, width, height);
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
}
