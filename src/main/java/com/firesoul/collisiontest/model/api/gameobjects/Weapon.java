package com.firesoul.collisiontest.model.api.gameobjects;

import com.firesoul.collisiontest.model.api.GameObject;

public interface Weapon extends GameObject {

    void attack();

    GameObject getHolder();

    void setDirectionX(double directionX);
}
