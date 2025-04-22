package test.model.impl;

import java.util.Objects;

import test.controller.InputController;
import test.model.api.GameObject;
import test.model.util.Vector2;

public class PlayerBuilder extends GameObjectBuilderImpl {

    private final GameObject sword;
    private final GameObject terrainDetector;
    private final InputController input;
    private final GameCollisions world;

    public PlayerBuilder(final Vector2 position, final GameObject sword, final GameObject terrainDetector, final InputController input, final GameCollisions world) {
        super(position);
        Objects.requireNonNull(sword);
        Objects.requireNonNull(terrainDetector);
        Objects.requireNonNull(input);
        Objects.requireNonNull(world);
        this.sword = sword;
        this.terrainDetector = terrainDetector;
        this.input = input;
        this.world = world;
    }

    @Override
    public GameObject build() {
        final GameObject builded = new Player(this.getPosition(), this.getOrientation(), this.getCollider(), this.getImage(), this.sword, this.terrainDetector, this.input, this.world);
        if (this.getCollider().isPresent()) {
            this.getCollider().get().attachGameObject(builded);
        }
        return builded;
    }
}
