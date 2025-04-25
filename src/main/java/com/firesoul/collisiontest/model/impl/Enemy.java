package com.firesoul.collisiontest.model.impl;

import java.awt.Image;
import java.util.Optional;

import com.firesoul.collisiontest.model.api.Collider;
import com.firesoul.collisiontest.model.util.Vector2;

public class Enemy extends GameObjectImpl {

    public Enemy(final Vector2 position, final double orientation, final Optional<Collider> collider, final Optional<Image> image) {
        super(position, orientation, true, collider, image);
    }
}
