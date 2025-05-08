package com.firesoul.collisiontest.controller.impl.wrappers;

import java.awt.Color;
import java.awt.Point;

import com.firesoul.collisiontest.controller.api.RenderableWrapper;
import com.firesoul.collisiontest.model.api.gameobjects.Camera;
import com.firesoul.collisiontest.model.impl.Rectangle;
import com.firesoul.collisiontest.model.util.Vector2;
import com.firesoul.collisiontest.view.api.Renderable;
import com.firesoul.collisiontest.view.impl.renderables.shapes.SwingRectangle;

public class RectangleWrapper implements RenderableWrapper {

    private final SwingRectangle renderable;
    private final Rectangle rect;

    public RectangleWrapper(final Rectangle rectangle) {
        this.rect = rectangle;

        final Point position = new Point((int) this.rect.getPosition().x(), (int) this.rect.getPosition().y());
        this.renderable = new SwingRectangle(position, this.rect.getWidth(), this.rect.getHeight(), new Color(this.rect.getColor()), this.rect.isVisible());
    }

    @Override
    public Renderable wrap(final Camera camera) {
        final Vector2 newPos = this.rect.getPosition().subtract(camera.getPosition());
        this.renderable.translate(newPos.x(), newPos.y());
        this.renderable.setVisible(this.rect.isVisible());
        return this.renderable;
    }
}
