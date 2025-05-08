package com.firesoul.collisiontest.view.api;

import java.awt.*;

public interface Renderable {

    Point getPosition();

    void mirrorX(double directionX);

    void scale(double scaleX, double scaleY);

    void translate(Point position);

    void translate(double x, double y);

    void rotate(double angle);

    void draw();

    int getWidth();

    int getHeight();

    void resize(int width, int height);

    boolean isVisible();

    void setVisible(boolean visible);
}
