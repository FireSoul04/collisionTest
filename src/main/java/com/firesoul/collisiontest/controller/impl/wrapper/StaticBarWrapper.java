package com.firesoul.collisiontest.controller.impl.wrapper;

import java.awt.Color;

import com.firesoul.collisiontest.controller.api.wrapper.RenderableWrapper;
import com.firesoul.collisiontest.model.api.gameobjects.Camera;
import com.firesoul.collisiontest.model.impl.drawable.ui.StaticBar;
import com.firesoul.collisiontest.view.api.Renderable;
import com.firesoul.collisiontest.view.impl.renderables.SwingBar;

public class StaticBarWrapper implements RenderableWrapper {

    private final SwingBar renderable;
    private final StaticBar bar;
    private final int rgba;

    public StaticBarWrapper(final StaticBar bar, final int rgba) {
        this.bar = bar;
        this.rgba = rgba;
        this.renderable = new SwingBar(this.bar.getWidth(), this.bar.getHeight(), new Color(this.rgba), this.bar.isVisible());
    }

    @Override
    public Renderable wrap(final Camera camera) {
        this.renderable.translate(this.bar.getPosition().x(), this.bar.getPosition().y());
        this.renderable.setCurrentPercentage(this.bar.getCurrentPercentage());
        this.renderable.setVisible(bar.isVisible());
        return this.renderable;
    }
}
