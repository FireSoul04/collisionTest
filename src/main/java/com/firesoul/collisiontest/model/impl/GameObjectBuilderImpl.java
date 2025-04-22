package test.model.impl;

import java.awt.Image;
import java.util.Objects;
import java.util.Optional;

import test.model.api.Collider;
import test.model.api.GameObject;
import test.model.api.GameObjectBuilder;
import test.model.util.Vector2;

public class GameObjectBuilderImpl implements GameObjectBuilder {

    private final Vector2 position;
    private double orientation;
    private Optional<Collider> collider;
    private Optional<Image> image;

    public GameObjectBuilderImpl(final Vector2 position) {
        Objects.requireNonNull(position);
        this.position = position;
        this.orientation = 0.0;
        this.image = Optional.empty();
    }

    @Override
    public GameObjectBuilder orientation(final double orientation) {
        if (!Double.isFinite(orientation)) {
            throw new IllegalStateException("Gameobject orientation must be a number");
        }
        this.orientation = orientation;
        return this;
    }

    @Override
    public GameObjectBuilder collider(final Collider collider) {
        Objects.requireNonNull(collider);
        this.collider = Optional.of(collider);
        return this;
    }

    @Override
    public GameObjectBuilder image(final Image image) {
        Objects.requireNonNull(image);
        this.image = Optional.of(image);
        return this;
    }

    @Override
    public GameObject build() {
        final GameObject builded = new GameObjectImpl(this.position, this.orientation, this.collider, this.image);
        if (this.getCollider().isPresent()) {
            this.collider.get().attachGameObject(builded);
        }
        return builded;
    }

    protected Vector2 getPosition() {
        return this.position;
    }

    protected Optional<Collider> getCollider() {
        return this.collider;
    }

    protected double getOrientation() {
        return this.orientation;
    }

    protected Optional<Image> getImage() {
        return this.image;
    }
}
