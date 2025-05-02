package com.firesoul.collisiontest.view.api;

import com.firesoul.collisiontest.model.util.Vector2;

public interface Drawable {

    public static final String RESOURCES_PATH = "src/main/resources/";

    void translate(Vector2 position);

    void rotate(double angle);

    void draw();

    boolean isVisible();

    void setVisible(boolean visible);
}
