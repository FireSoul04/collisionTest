package com.firesoul.collisiontest.model.api;

import com.firesoul.collisiontest.model.api.gameobjects.Weapon;
import com.firesoul.collisiontest.model.impl.gameobjects.Player;

public interface WeaponFactory {

    Weapon sword(Player holder);

    Weapon gun(Player holder);
}
