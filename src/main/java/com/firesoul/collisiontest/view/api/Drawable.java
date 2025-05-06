package com.firesoul.collisiontest.view.api;

import com.firesoul.collisiontest.model.util.Vector2;

public interface Drawable {

    String RESOURCES_PATH = "src/main/resources/";

    Vector2 getPosition();

    void mirrorX(double directionX);

    void scale(Vector2 scale);

    void translate(Vector2 position);

    void rotate(double angle);

    void draw();

    int getWidth();

    int getHeight();

    boolean isVisible();

    void setVisible(boolean visible);
}
