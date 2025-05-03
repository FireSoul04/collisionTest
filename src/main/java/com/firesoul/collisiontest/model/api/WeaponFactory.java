package com.firesoul.collisiontest.model.api;

import com.firesoul.collisiontest.model.impl.GameCollisions;
import com.firesoul.collisiontest.model.impl.Player;

public interface WeaponFactory {

    Weapon sword(Player holder);

    Weapon gun(Player holder, GameCollisions world);
}
