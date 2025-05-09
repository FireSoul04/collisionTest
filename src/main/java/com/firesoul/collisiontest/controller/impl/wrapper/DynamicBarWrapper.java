package com.firesoul.collisiontest.controller.impl.wrapper;

import java.awt.Color;

import com.firesoul.collisiontest.controller.api.wrapper.RenderableWrapper;
import com.firesoul.collisiontest.model.api.gameobjects.Camera;
import com.firesoul.collisiontest.model.impl.drawable.ui.DynamicBar;
import com.firesoul.collisiontest.model.util.Vector2;
import com.firesoul.collisiontest.view.api.Renderable;
import com.firesoul.collisiontest.view.impl.renderables.SwingBar;

public class DynamicBarWrapper implements RenderableWrapper {

    private final SwingBar renderable;
    private final DynamicBar bar;
    private final int rgba;

    public DynamicBarWrapper(final DynamicBar bar, final int rgba) {
        this.bar = bar;
        this.rgba = rgba;
        this.renderable = new SwingBar(this.bar.getWidth(), this.bar.getHeight(), new Color(this.rgba), this.bar.isVisible());
    }

    @Override
    public Renderable wrap(final Camera camera) {
        final Vector2 newPos = this.bar.getPosition().subtract(camera.getPosition());
        this.renderable.translate(newPos.x(), newPos.y());
        this.renderable.setCurrentPercentage(this.bar.getCurrentPercentage());
        this.renderable.setVisible(this.bar.isVisible());
        return this.renderable;
    }
}
