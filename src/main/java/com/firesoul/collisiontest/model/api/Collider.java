package test.model.api;

import java.util.List;
import java.util.Set;

import test.model.util.Vector2;

public interface Collider {

    void move(Vector2 position);

    void rotate(double angle);

    List<Vector2> getPoints();

    Set<Collider> getCollidedShapes();

    void attachGameObject(GameObject gameObject);

    GameObject getAttachedGameObject();

    double getOrientation();

    Vector2 getPosition();

    boolean isCollided();

    void addCollided(Collider collidedShape);

    void removeCollided(Collider collidedShape);

    boolean isSolid();

    void setSolid(boolean solid);
}
