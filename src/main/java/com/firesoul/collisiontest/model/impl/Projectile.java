package test.model.impl;

import java.awt.Image;
import java.util.Optional;

import test.model.api.Collider;
import test.model.util.Vector2;

public class Projectile extends GameObjectImpl {

    private final double speed;

    public Projectile(final Vector2 position, final double orientation, final Optional<Collider> collider, final Optional<Image> image, final double speed) {
        super(position, orientation, collider, image);
        this.speed = speed;
    }
    
    @Override
    public void update(final double deltaTime) {
        this.move(new Vector2(this.speed, 0.0));
    }
}
