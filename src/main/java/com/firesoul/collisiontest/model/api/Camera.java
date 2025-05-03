package com.firesoul.collisiontest.model.api;

import com.firesoul.collisiontest.model.impl.GameCollisions;

public interface Camera extends GameObject {

    int getWidth();

    int getHeight();

    void setBoundsX(double boundsX);

    void setBoundsY(double boundsY);
}
