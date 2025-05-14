package com.firesoul.collisiontest.controller.api.loader;

import com.firesoul.collisiontest.model.api.drawable.ui.Bar;
import com.firesoul.collisiontest.model.api.drawable.Drawable;
import com.firesoul.collisiontest.model.api.gameobjects.Camera;
import com.firesoul.collisiontest.model.util.Vector2;
import com.firesoul.collisiontest.view.api.Renderable;

public interface DrawableLoader {

	void update();

	Renderable getFromDrawable(Drawable drawable, Camera camera);

	Drawable loadSpriteFromSystem(String path);

	Drawable loadLabel(Vector2 position, String string, int rgba);

	Drawable loadRectangle(Vector2 position, int width, int height, int rgba);

	Drawable loadRectangleBorder(Vector2 position, int width, int height, int rgba);

	Bar loadStaticBar(Vector2 position, int width, int height, int rgba);

	Bar loadDynamicBar(int width, int height, int rgba);
}
