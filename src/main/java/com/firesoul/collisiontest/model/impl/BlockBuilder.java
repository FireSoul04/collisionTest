package com.firesoul.collisiontest.model.impl;

import java.awt.Image;
import java.util.Optional;

import com.firesoul.collisiontest.model.api.Collider;
import com.firesoul.collisiontest.model.api.GameObject;
import com.firesoul.collisiontest.model.util.Vector2;

public class BlockBuilder extends GameObjectBuilderImpl {

    public class Block extends GameObjectImpl {

        public Block(final Vector2 position, final double orientation, final Optional<Collider> collider, final Optional<Image> image) {
            super(position, orientation, collider, image);
        }
    }
    
    public BlockBuilder(final Vector2 position) {
        super(position);
    }

    @Override
    public GameObject build() {
        final GameObject builded = new Block(this.getPosition(), this.getOrientation(), this.getCollider(), this.getImage());
        if (this.getCollider().isPresent()) {
            this.getCollider().get().attachGameObject(builded);
        }
        return builded;
    }
}
