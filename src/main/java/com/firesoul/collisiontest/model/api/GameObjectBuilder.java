package com.firesoul.collisiontest.model.api;

import com.firesoul.collisiontest.model.api.physics.Collider;

public interface GameObjectBuilder {

    GameObjectBuilder collider(Collider collider);

    GameObjectBuilder sprite(Drawable sprite);

    GameObject build();
}
