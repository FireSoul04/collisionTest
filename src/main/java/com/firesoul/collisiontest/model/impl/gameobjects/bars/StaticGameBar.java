package com.firesoul.collisiontest.model.impl.gameobjects.bars;

import com.firesoul.collisiontest.model.api.Level;
import com.firesoul.collisiontest.model.util.Vector2;
import com.firesoul.collisiontest.model.api.Bar;

public class StaticGameBar extends GameBar {

	private final Vector2 barPosition;

	public StaticGameBar(final Vector2 position, final Level world, final Bar sprite, final double maxValue, final boolean inverse) {
		super(position, world, sprite, maxValue, inverse);
		this.barPosition = position;
	}

	public StaticGameBar(final Vector2 position, final Level world, final Bar sprite, final double maxValue) {
		this(position, world, sprite, maxValue, false);
	}

	@Override
	public void update(final double deltaTime) {
		this.setPosition(this.getWorld().getCamera().getPosition().add(new Vector2(
			this.getWorld().getCamera().getWidth(),
			this.getWorld().getCamera().getHeight()
		).divide(2.0)));
		this.getBar().translate(this.barPosition);
	}
}
