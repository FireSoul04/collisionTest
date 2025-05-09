package com.firesoul.collisiontest.model.impl.gameobjects.bars;

import com.firesoul.collisiontest.model.api.GameObject;
import com.firesoul.collisiontest.model.api.Level;
import com.firesoul.collisiontest.model.util.Vector2;
import com.firesoul.collisiontest.model.api.drawable.ui.Bar;

import java.util.Optional;
import java.util.function.Function;

public class AttachedBar extends DynamicGameBar {

	private final Function<GameObject, Vector2> offset;
	private Optional<GameObject> attachedGameObject = Optional.empty();

	public AttachedBar(final Function<GameObject, Vector2> offset, final Level world, final Bar sprite, final double maxValue, final boolean inverse) {
		super(() -> Vector2.zero(), world, sprite, maxValue, inverse);
		this.offset = offset;
	}

	public AttachedBar(final Function<GameObject, Vector2> offset, final Level world, final Bar sprite, final double maxValue) {
		this(offset, world, sprite, maxValue, false);
	}

	@Override
	public void update(final double deltaTime) {
		this.attachedGameObject.ifPresent(t -> this.setPosition(
			t.getPosition()
				.subtract(new Vector2(this.getBar().getWidth() / 2.0, 0.0))
				.add(this.offset.apply(t))	
		));
	}

	public void attachedGameObject(final GameObject gameObject) {
		this.attachedGameObject = Optional.of(gameObject);
	}
}
