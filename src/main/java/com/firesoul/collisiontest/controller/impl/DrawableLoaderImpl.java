package com.firesoul.collisiontest.controller.impl;

import com.firesoul.collisiontest.controller.api.DrawableLoader;
import com.firesoul.collisiontest.model.api.Bar;
import com.firesoul.collisiontest.model.api.Drawable;
import com.firesoul.collisiontest.model.api.UI;
import com.firesoul.collisiontest.model.api.gameobjects.Camera;
import com.firesoul.collisiontest.model.impl.Sprite;
import com.firesoul.collisiontest.model.impl.StaticBar;
import com.firesoul.collisiontest.model.util.Vector2;
import com.firesoul.collisiontest.view.api.RenderableBar;
import com.firesoul.collisiontest.view.api.Renderable;
import com.firesoul.collisiontest.view.impl.SwingBar;
import com.firesoul.collisiontest.view.impl.SwingSprite;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DrawableLoaderImpl implements DrawableLoader {

	private static final String RESOURCES_PATH = "src/main/resources/";

	private final Map<Drawable, Renderable> wrapper = new HashMap<>();

	@Override
	public Renderable getFromDrawable(final Drawable drawable, final Camera camera) {
		final Renderable renderable = this.wrapper.get(drawable);
		final Vector2 newPos;
		if (drawable instanceof UI ui) {
			newPos = drawable.getPosition();
			if (ui instanceof Bar b && renderable instanceof RenderableBar rb) {
				rb.setCurrentPercentage(b.getCurrentPercentage());
			}
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
		this.wrapper.put(sprite, new SwingSprite(image));
		return sprite;
	}

	@Override
	public Bar loadStaticBar(final Vector2 position, final int width, final int height, final int rgba) {
		final Bar bar = new StaticBar(position, width, height);
		this.wrapper.put(bar, new SwingBar(width, height, new Color(rgba), true));
		return bar;
	}
}
