package com.firesoul.collisiontest.controller.impl.wrapper;

import java.awt.Color;

import com.firesoul.collisiontest.model.api.drawable.ui.Bar;
import com.firesoul.collisiontest.model.api.gameobjects.Camera;
import com.firesoul.collisiontest.model.impl.drawable.ui.StaticBar;
import com.firesoul.collisiontest.view.api.Renderable;
import com.firesoul.collisiontest.view.impl.renderables.SwingBar;

public class StaticBarWrapper extends AbstractRenderableWrapper {

    public StaticBarWrapper(final StaticBar bar, final int rgba) {
        super(bar, new SwingBar(
            bar.getWidth(), bar.getHeight(),
            new Color(rgba), bar.isVisible())
        );
    }

    @Override
    public Renderable wrap(final Camera camera) {
        ((SwingBar) this.getRenderable()).setCurrentPercentage(((Bar) this.getDrawable()).getCurrentPercentage());
        this.getRenderable().translate(this.getDrawable().getPosition().x(), this.getDrawable().getPosition().y());
        this.getRenderable().setVisible(this.getDrawable().isVisible());
        return this.getRenderable();
    }
}
