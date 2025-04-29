package com.firesoul.collisiontest.model.api;

public interface Entity extends GameObject {

    void takeDamage(int amount);

    void onDestroy();

    boolean isInvincible();
}
