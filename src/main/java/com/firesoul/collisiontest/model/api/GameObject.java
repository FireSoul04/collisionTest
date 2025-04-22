package test.model.api;

import java.awt.Image;
import java.util.Optional;

import test.model.util.Vector2;

public interface GameObject {

    void update(double deltaTime);

    void rotate(double angle);

    void move(Vector2 position);

    void setSolid(boolean solid);

    Vector2 getPosition();

    double getOrientation();

    Optional<Image> getImage();

    Optional<Collider> getCollider();
}
