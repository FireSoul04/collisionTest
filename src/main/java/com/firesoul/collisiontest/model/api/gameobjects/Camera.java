package com.firesoul.collisiontest.model.api.gameobjects;

import com.firesoul.collisiontest.model.api.GameObject;

public interface Camera extends GameObject {

    int getWidth();

    int getHeight();

    void setBoundsX(double boundsX);

    void setBoundsY(double boundsY);
}
