package com.firesoul.collisiontest.model.api;

import com.firesoul.collisiontest.view.api.Drawable;

public interface GameObjectBuilder {

    GameObjectBuilder orientation(double orientation);

    GameObjectBuilder collider(Collider collider);

    GameObjectBuilder sprite(Drawable sprite);

    GameObject build();
}
