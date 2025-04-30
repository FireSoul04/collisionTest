package com.firesoul.collisiontest.model.api;

import com.firesoul.collisiontest.controller.impl.InputController;
import com.firesoul.collisiontest.model.impl.GameCollisions;
import com.firesoul.collisiontest.model.util.Vector2;

public interface GameObjectFactory {

    GameObject player(Vector2 position, InputController input, GameCollisions world);

    GameObject projectile(Vector2 position, double speed);

    GameObject ballEnemy(Vector2 position);

    GameObject block(Vector2 position);
}
