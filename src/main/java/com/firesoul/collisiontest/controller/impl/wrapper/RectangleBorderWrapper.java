package com.firesoul.collisiontest.controller.impl.wrapper;

import java.awt.Color;
import java.awt.Point;

import com.firesoul.collisiontest.controller.api.wrapper.RenderableWrapper;
import com.firesoul.collisiontest.model.api.gameobjects.Camera;
import com.firesoul.collisiontest.model.impl.drawable.primitives.Rectangle;
import com.firesoul.collisiontest.model.util.Vector2;
import com.firesoul.collisiontest.view.api.Renderable;
import com.firesoul.collisiontest.view.impl.renderables.shapes.SwingRectangleBorder;

public class RectangleBorderWrapper implements RenderableWrapper {

    private final SwingRectangleBorder renderable;
    private final Rectangle rect;

    public RectangleBorderWrapper(final Rectangle rectangle) {
        this.rect = rectangle;

        final Point position = new Point((int) this.rect.getPosition().x(), (int) this.rect.getPosition().y());
        this.renderable = new SwingRectangleBorder(position, this.rect.getWidth(), this.rect.getHeight(), new Color(this.rect.getColor()), this.rect.isVisible());
    }

    @Override
    public Renderable wrap(final Camera camera) {
        final Vector2 newPos = this.rect.getPosition().subtract(camera.getPosition());
        this.renderable.translate(newPos.x(), newPos.y());
        this.renderable.setVisible(this.rect.isVisible());
        return this.renderable;
    }
}
