package com.firesoul.collisiontest.controller.impl.loader;

import com.firesoul.collisiontest.controller.api.loader.DrawableLoader;
import com.firesoul.collisiontest.controller.api.wrapper.RenderableWrapper;
import com.firesoul.collisiontest.controller.impl.wrapper.*;
import com.firesoul.collisiontest.model.api.drawable.ui.Bar;
import com.firesoul.collisiontest.model.api.drawable.Drawable;
import com.firesoul.collisiontest.model.api.gameobjects.Camera;
import com.firesoul.collisiontest.model.impl.drawable.ui.DynamicBar;
import com.firesoul.collisiontest.model.impl.drawable.primitives.Rectangle;
import com.firesoul.collisiontest.model.impl.drawable.Sprite;
import com.firesoul.collisiontest.model.impl.drawable.ui.StaticBar;
import com.firesoul.collisiontest.model.impl.drawable.ui.Label;
import com.firesoul.collisiontest.model.util.Vector2;
import com.firesoul.collisiontest.view.api.Renderable;

import javax.imageio.ImageIO;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DrawableLoaderImpl implements DrawableLoader {

	private static final String RESOURCES_PATH = "src/main/resources/sprites/";

	private final Map<Drawable, RenderableWrapper> wrapper = new HashMap<>();
	private final Map<Drawable, RenderableWrapper> wrapperQ = new HashMap<>();

	@Override
	public void update() {
		this.wrapper.putAll(this.wrapperQ);
		this.wrapperQ.clear();
	}

	@Override
	public Renderable getFromDrawable(final Drawable drawable, final Camera camera) {
		return this.wrapper.get(drawable).wrap(camera);
	}

	@Override
	public Drawable loadSpriteFromSystem(final String path) {
		final Image image;
		try {
			image = ImageIO.read(new File(RESOURCES_PATH + path + ".png"));
		} catch (IOException e) {
			throw new IllegalStateException("Couldn't read sprite in " + path + ": " + e);
		}
		final Sprite sprite = new Sprite(image.getWidth(null), image.getHeight(null));
		this.wrapperQ.put(sprite, new SpriteWrapper(sprite, image));
		return sprite;
	}

	@Override
	public Drawable loadLabel(final Vector2 position, final String string) {
		final Label label = new Label(position, string);
		this.wrapperQ.put(label, new LabelWrapper(label));
		return label;
	}

	@Override
	public Drawable loadRectangle(final Vector2 position, final int width, final int height, final int rgba) {
		final Rectangle rect = new Rectangle(position, width, height, rgba);
		this.wrapperQ.put(rect, new RectangleWrapper(rect));
		return rect;
	}

	@Override
	public Drawable loadRectangleBorder(final Vector2 position, final int width, final int height, final int rgba) {
		final Rectangle rect = new Rectangle(position, width, height, rgba);
		this.wrapperQ.put(rect, new RectangleBorderWrapper(rect));
		return rect;
	}

	@Override
	public Bar loadStaticBar(final Vector2 position, final int width, final int height, final int rgba) {
		final StaticBar bar = new StaticBar(position, width, height);
		this.wrapperQ.put(bar, new StaticBarWrapper(bar, rgba));
		return bar;
	}

	@Override
	public Bar loadDynamicBar(final int width, final int height, final int rgba) {
		final DynamicBar bar = new DynamicBar(width, height);
		this.wrapperQ.put(bar, new DynamicBarWrapper(bar, rgba));
		return bar;
	}
}
