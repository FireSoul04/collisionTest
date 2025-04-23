package com.firesoul.collisiontest.model.impl;

import java.util.Objects;

import com.firesoul.collisiontest.controller.impl.InputController;
import com.firesoul.collisiontest.model.api.GameObject;
import com.firesoul.collisiontest.model.util.Vector2;

public class PlayerBuilder extends GameObjectBuilderImpl {

    private final GameObject sword;
    private final InputController input;
    private final GameCollisions world;

    public PlayerBuilder(final Vector2 position, final GameObject sword, final InputController input, final GameCollisions world) {
        super(position);
        Objects.requireNonNull(sword);
        Objects.requireNonNull(input);
        Objects.requireNonNull(world);
        this.sword = sword;
        this.input = input;
        this.world = world;
    }

    @Override
    public GameObject build() {
        final GameObject builded = new Player(this.getPosition(), this.getOrientation(), this.getCollider(), this.getImage(), this.sword, this.input, this.world);
        if (this.getCollider().isPresent()) {
            this.getCollider().get().attachGameObject(builded);
        }
        return builded;
    }
}
