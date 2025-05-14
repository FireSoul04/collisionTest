package com.firesoul.collisiontest.model.api.factories;

import com.firesoul.collisiontest.controller.api.EventManager;
import com.firesoul.collisiontest.model.api.GameObject;
import com.firesoul.collisiontest.model.impl.gameobjects.Player;
import com.firesoul.collisiontest.model.util.Vector2;

public interface GameObjectFactory {

    Player player(Vector2 position, EventManager<String> input);

    GameObject projectile(Vector2 position, double speed);

    GameObject groundEnemy(Vector2 position);

    GameObject flyingEnemy(Vector2 position, double range, double speed);

    GameObject block(Vector2 position);
}
