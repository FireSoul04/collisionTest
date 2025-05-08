package com.firesoul.collisiontest.controller.impl;

import com.firesoul.collisiontest.controller.api.DrawableLoader;
import com.firesoul.collisiontest.model.api.Bar;
import com.firesoul.collisiontest.model.api.Drawable;
import com.firesoul.collisiontest.model.api.UI;
import com.firesoul.collisiontest.model.api.gameobjects.Camera;
import com.firesoul.collisiontest.model.impl.DynamicBar;
import com.firesoul.collisiontest.model.impl.Rectangle;
import com.firesoul.collisiontest.model.impl.Sprite;
import com.firesoul.collisiontest.model.impl.StaticBar;
import com.firesoul.collisiontest.model.util.Vector2;
import com.firesoul.collisiontest.view.api.RenderableBar;
import com.firesoul.collisiontest.view.api.Renderable;
import com.firesoul.collisiontest.view.impl.renderables.SwingBar;
import com.firesoul.collisiontest.view.impl.renderables.SwingSprite;
import com.firesoul.collisiontest.view.impl.renderables.shapes.SwingRectangle;
import com.firesoul.collisiontest.view.impl.renderables.shapes.SwingRectangleBorder;

import javax.imageio.ImageIO;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DrawableLoaderImpl implements DrawableLoader {

	private static final String RESOURCES_PATH = "src/main/resources/";

	private final Map<Drawable, Renderable> wrapper = new HashMap<>();
	private final Map<Drawable, Renderable> wrapperQ = new HashMap<>();

	@Override
	public void update() {
		this.wrapper.putAll(this.wrapperQ);
		this.wrapperQ.clear();
	}

	@Override
	public Renderable getFromDrawable(final Drawable drawable, final Camera camera) {
		final Renderable renderable = this.wrapper.get(drawable);
		final Vector2 newPos;
		if (drawable instanceof Bar b && renderable instanceof RenderableBar rb) {
			rb.setCurrentPercentage(b.getCurrentPercentage());
		}
		if (drawable instanceof UI) {
			newPos = drawable.getPosition();
		} else {
			newPos = drawable.getPosition().subtract(camera.getPosition());
		}
		renderable.translate(newPos.x(), newPos.y());
//		renderable.rotate(drawable.getOrientation());
//		renderable.scale(drawable.getScale().x(), drawable.getScale().y());
		renderable.setVisible(drawable.isVisible());
		renderable.mirrorX(drawable.getDirectionX());
		return renderable;
	}

	@Override
	public Drawable loadSpriteFromSystem(final String path) {
		final Image image;
		try {
			image = ImageIO.read(new File(RESOURCES_PATH + path + ".png"));
		} catch (IOException e) {
			throw new IllegalStateException("Couldn't read sprite in " + path + ": " + e);
		}
		final Drawable sprite = new Sprite(image.getWidth(null), image.getHeight(null));
		this.wrapperQ.put(sprite, new SwingSprite(image));
		return sprite;
	}

	@Override
	public Drawable loadRectangle(final Vector2 position, final int width, final int height, final int rgba) {
		final Drawable rect = new Rectangle(position, width, height);
		this.wrapperQ.put(rect, new SwingRectangle(new Point(), width, height, new Color(rgba), true));
		return rect;
	}

	@Override
	public Drawable loadRectangleBorder(final Vector2 position, final int width, final int height, final int rgba) {
		final Drawable rect = new Rectangle(position, width, height);
		this.wrapperQ.put(rect, new SwingRectangleBorder(new Point(), width, height, new Color(rgba), true));
		return rect;
	}

	@Override
	public Bar loadStaticBar(final Vector2 position, final int width, final int height, final int rgba) {
		final Bar bar = new StaticBar(position, width, height);
		this.wrapperQ.put(bar, new SwingBar(width, height, new Color(rgba), true));
		return bar;
	}

	@Override
	public Bar loadDynamicBar(final int width, final int height, final int rgba) {
		final Bar bar = new DynamicBar(width, height);
		this.wrapperQ.put(bar, new SwingBar(width, height, new Color(rgba), true));
		return bar;
	}
}
