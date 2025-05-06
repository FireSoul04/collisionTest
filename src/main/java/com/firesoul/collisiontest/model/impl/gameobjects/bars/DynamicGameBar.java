package com.firesoul.collisiontest.model.impl.gameobjects.bars;

import com.firesoul.collisiontest.model.api.Level;
import com.firesoul.collisiontest.model.util.Vector2;
import com.firesoul.collisiontest.view.api.Bar;

import java.util.function.Supplier;

public class DynamicGameBar extends GameBar {

	private final Supplier<Vector2> update;

	public DynamicGameBar(final Supplier<Vector2> update, final Level world, final Bar sprite, final double maxValue, final boolean inverse) {
		super(Vector2.zero(), world, sprite, maxValue, inverse);
		this.update = update;
	}

	public DynamicGameBar(final Supplier<Vector2> update, final Level world, final Bar sprite, final double maxValue) {
		this(update, world, sprite, maxValue, false);
	}

	@Override
	public void update(final double deltaTime) {
		this.setPosition(this.update.get());
	}
}
