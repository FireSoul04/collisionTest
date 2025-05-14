package com.firesoul.collisiontest.model.impl.drawable.ui;

import com.firesoul.collisiontest.model.util.Vector2;

public class Label extends UIBase {

	private String text;

	public Label(final Vector2 position, final String text) {
		super(position, 0, 0);
		this.text = text;
	}

	public void setText(final String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}
}
