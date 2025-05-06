package com.firesoul.collisiontest.model.impl.gameobjects.bars;

import com.firesoul.collisiontest.model.api.GameObject;
import com.firesoul.collisiontest.model.api.Level;
import com.firesoul.collisiontest.model.util.Vector2;
import com.firesoul.collisiontest.view.api.Bar;

import java.util.function.Function;

public class AttachedBar extends DynamicGameBar {

	public AttachedBar(final GameObject attachedGameObject, final Function<GameObject, Vector2> offset, final Level world, final Bar sprite, final double maxValue, final boolean inverse) {
		super(
			() -> attachedGameObject.getPosition().subtract(new Vector2(sprite.getWidth() / 2.0, 0.0))
				.add(offset.apply(attachedGameObject)),
			world, sprite, maxValue, inverse
		);
	}

	public AttachedBar(final GameObject attachedGameObject, final Function<GameObject, Vector2> offset, final Level world, final Bar sprite, final double maxValue) {
		this(attachedGameObject, offset, world, sprite, maxValue, false);
	}
}
