//package com.firesoul.collisiontest.view.impl;
//
//import com.firesoul.collisiontest.view.api.Drawable;
//import com.firesoul.collisiontest.model.api.DrawableFactory;
//import com.firesoul.collisiontest.view.api.Bar;
//import com.firesoul.collisiontest.view.api.Renderer;
//
//import java.awt.*;
//
//public class SwingDrawableFactory implements DrawableFactory {
//
//	private final Renderer renderer;
//
//	public SwingDrawableFactory(final Renderer renderer) {
//		this.renderer = renderer;
//	}
//
//	@Override
//	public Drawable spriteByName(final String name, final Point position) {
//		return this.baseSpriteByName(name, position, true);
//	}
//
//	@Override
//	public Drawable invisibleSpriteByName(final String name, final Point position) {
//		return this.baseSpriteByName(name, position, false);
//	}
//
//	@Override
//	public Bar staticBar(final int width, final int height, final int rgba) {
//		return this.baseStaticBar(width, height, rgba, true);
//	}
//
//	@Override
//	public Bar invisibleStaticBar(final int width, final int height, final int rgba) {
//		return this.baseStaticBar(width, height, rgba, false);
//	}
//
//	@Override
//	public Bar dynamicBar(final int width, final int height, final int rgba) {
//		return this.baseDynamicBar(width, height, rgba, true);
//	}
//
//	@Override
//	public Bar invisibleDynamicBar(final int width, final int height, final int rgba) {
//		return this.baseDynamicBar(width, height, rgba, false);
//	}
//
//	private Drawable baseSpriteByName(final String name, final Point position, final boolean visible) {
//		final Drawable sprite = new SwingSprite(name, position, 0.0, visible);
//		this.renderer.add(sprite);
//		return sprite;
//	}
//
//	private Bar baseStaticBar(final int width, final int height, final int rgba, final boolean visible) {
//		return this.bar(width, height, rgba, visible, true);
//	}
//
//	private Bar baseDynamicBar(final int width, final int height, final int rgba, final boolean visible) {
//		return this.bar(width, height, rgba, visible, false);
//	}
//
//	private Bar bar(final int width, final int height, final int rgba, final boolean visible, final boolean staticc) {
//		final Bar bar = new SwingBar(width, height, new Color(rgba), visible, staticc);
//		this.renderer.add(bar);
//		return bar;
//	}
//}
