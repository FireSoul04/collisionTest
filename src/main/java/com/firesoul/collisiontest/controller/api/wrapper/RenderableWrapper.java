package com.firesoul.collisiontest.controller.api.wrapper;

import com.firesoul.collisiontest.model.api.gameobjects.Camera;
import com.firesoul.collisiontest.view.api.Renderable;

public interface RenderableWrapper {

    Renderable wrap(Camera camera);
}
