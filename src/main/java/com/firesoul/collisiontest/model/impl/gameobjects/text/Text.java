package com.firesoul.collisiontest.model.impl.gameobjects.text;

import com.firesoul.collisiontest.model.api.Level;
import com.firesoul.collisiontest.model.api.drawable.Drawable;
import com.firesoul.collisiontest.model.impl.drawable.ui.Label;
import com.firesoul.collisiontest.model.impl.gameobjects.GameObjectImpl;
import com.firesoul.collisiontest.model.util.Vector2;

import java.util.Optional;

public class Text extends GameObjectImpl {

	public Text(final Vector2 position, final Level world, final Drawable sprite) {
		super(position, false, world, Optional.empty(), Optional.of(sprite));
	}

	public void setText(final String text) {
		((Label) this.getSprite().orElseThrow()).setText(text);
	}
}
