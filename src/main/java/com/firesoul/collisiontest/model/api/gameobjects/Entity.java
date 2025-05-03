package com.firesoul.collisiontest.model.api.gameobjects;

import com.firesoul.collisiontest.model.api.GameObject;

public interface Entity extends GameObject {

    void takeDamage(int amount);

    void onDestroy();

    boolean isInvincible();
}
