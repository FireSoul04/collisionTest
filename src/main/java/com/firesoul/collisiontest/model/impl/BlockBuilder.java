package test.model.impl;

import java.awt.Image;
import java.util.Optional;

import test.model.api.Collider;
import test.model.api.GameObject;
import test.model.util.Vector2;

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
