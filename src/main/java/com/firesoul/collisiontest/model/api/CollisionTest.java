package test.model.api;

import java.util.List;

public interface CollisionTest {

    void render();

    void update(double deltaTime);

    List<GameObject> getGameObjects();
}
