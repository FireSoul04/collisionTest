package com.firesoul.collisiontest.controller.impl.wrappers;

import com.firesoul.collisiontest.controller.api.RenderableWrapper;
import com.firesoul.collisiontest.model.api.gameobjects.Camera;
import com.firesoul.collisiontest.model.impl.Sprite;
import com.firesoul.collisiontest.model.util.Vector2;
import com.firesoul.collisiontest.view.api.Renderable;
import com.firesoul.collisiontest.view.impl.renderables.SwingSprite;

import java.awt.*;

public class SpriteWrapper implements RenderableWrapper {

    private final SwingSprite renderable;
    private final Sprite sprite;

    public SpriteWrapper(final Sprite sprite, final Image image) {
        this.sprite = sprite;
        this.renderable = new SwingSprite(image);
    }

    @Override
    public Renderable wrap(final Camera camera) {
        final Vector2 newPos = this.sprite.getPosition().subtract(camera.getPosition());
        this.renderable.translate(newPos.x(), newPos.y());
        this.renderable.setVisible(this.sprite.isVisible());
        this.renderable.mirrorX(this.sprite.getDirectionX());
        return this.renderable;
    }
}
