package com.firesoul.collisiontest.model.api;

import com.firesoul.collisiontest.controller.impl.InputController;
import com.firesoul.collisiontest.model.impl.gameobjects.Player;
import com.firesoul.collisiontest.model.util.Vector2;

public interface GameObjectFactory {

    Player player(Vector2 position, InputController input);

    GameObject projectile(Vector2 position, double speed);

    GameObject groundEnemy(Vector2 position);

    GameObject flyingEnemy(Vector2 position, double range, double speed);

    GameObject block(Vector2 position);
}
