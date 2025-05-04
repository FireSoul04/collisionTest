package com.firesoul.collisiontest.model.api.gameobjects;

import com.firesoul.collisiontest.model.api.GameObject;

public interface Entity extends GameObject {

    int getLife();

    void setLife(int life);

    void addLife(int amount);

    void takeDamage(int amount);

    void onDestroy();

    boolean isInvincible();
}
