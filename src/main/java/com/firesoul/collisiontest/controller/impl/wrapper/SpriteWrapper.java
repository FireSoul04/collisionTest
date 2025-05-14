package com.firesoul.collisiontest.controller.impl.wrapper;

import com.firesoul.collisiontest.model.api.gameobjects.Camera;
import com.firesoul.collisiontest.model.impl.drawable.Sprite;
import com.firesoul.collisiontest.view.api.Renderable;
import com.firesoul.collisiontest.view.impl.renderables.SwingSprite;

import java.awt.*;

public class SpriteWrapper extends AbstractRenderableWrapper {

    public SpriteWrapper(final Sprite sprite, final Image image) {
        super(sprite, new SwingSprite(image));
    }

    @Override
    public Renderable wrap(final Camera camera) {
        this.getRenderable().mirrorX(this.getDrawable().getDirectionX());
        return super.wrap(camera);
    }
}
