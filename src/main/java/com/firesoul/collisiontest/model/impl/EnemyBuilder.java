package com.firesoul.collisiontest.model.impl;

import com.firesoul.collisiontest.model.api.GameObject;
import com.firesoul.collisiontest.model.util.Vector2;

public class EnemyBuilder extends GameObjectBuilderImpl {

    public EnemyBuilder(final Vector2 position) {
        super(position, true);
    }

    @Override
    public GameObject build() {
        final GameObject builded = new Enemy(this.getPosition(), this.getOrientation(), this.getCollider(), this.getImage());
        if (this.getCollider().isPresent()) {
            this.getCollider().get().attachGameObject(builded);
        }
        return builded;
    }
}
