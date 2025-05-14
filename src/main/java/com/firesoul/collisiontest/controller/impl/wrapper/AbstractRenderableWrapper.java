package com.firesoul.collisiontest.controller.impl.wrapper;

import com.firesoul.collisiontest.controller.api.wrapper.RenderableWrapper;
import com.firesoul.collisiontest.model.api.drawable.Drawable;
import com.firesoul.collisiontest.model.api.gameobjects.Camera;
import com.firesoul.collisiontest.model.util.Vector2;
import com.firesoul.collisiontest.view.api.Renderable;

public abstract class AbstractRenderableWrapper implements RenderableWrapper {

	private final Drawable drawable;
	private final Renderable renderable;

	public AbstractRenderableWrapper(final Drawable drawable, final Renderable renderable) {
		this.drawable = drawable;
		this.renderable = renderable;
	}

	@Override
	public Renderable wrap(final Camera camera) {
		final Vector2 newPos = this.drawable.getPosition().subtract(camera.getPosition());
		this.renderable.translate(newPos.x(), newPos.y());
		this.renderable.setVisible(this.drawable.isVisible());
		return this.renderable;
	}

	protected Drawable getDrawable() {
		return this.drawable;
	}

	protected Renderable getRenderable() {
		return this.renderable;
	}
}
