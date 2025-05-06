package com.firesoul.collisiontest.model.api;

import com.firesoul.collisiontest.model.util.Vector2;

public interface Drawable {

	Vector2 getPosition();

	void mirrorX(double directionX);

	void scale(Vector2 scale);

	void translate(Vector2 position);

	void rotate(double angle);

	int getWidth();

	int getHeight();

	boolean isVisible();

	void setVisible(boolean visible);
}
