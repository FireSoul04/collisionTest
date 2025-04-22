package test.model.api;

import java.awt.Image;

public interface GameObjectBuilder {

    GameObjectBuilder orientation(double orientation);

    GameObjectBuilder collider(Collider collider);

    GameObjectBuilder image(Image image);

    GameObject build();
}
