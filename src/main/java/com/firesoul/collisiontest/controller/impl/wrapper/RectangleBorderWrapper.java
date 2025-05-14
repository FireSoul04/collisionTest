package com.firesoul.collisiontest.controller.impl.wrapper;

import java.awt.Color;
import java.awt.Point;

import com.firesoul.collisiontest.model.impl.drawable.primitives.Rectangle;
import com.firesoul.collisiontest.view.impl.renderables.shapes.SwingRectangleBorder;

public class RectangleBorderWrapper extends AbstractRenderableWrapper {

    public RectangleBorderWrapper(final Rectangle rectangle) {
        super(rectangle, new SwingRectangleBorder(
            new Point((int) rectangle.getPosition().x(), (int) rectangle.getPosition().y()),
            rectangle.getWidth(), rectangle.getHeight(), new Color(rectangle.getColor()), rectangle.isVisible())
        );
    }
}
