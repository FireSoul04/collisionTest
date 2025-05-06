package com.firesoul.collisiontest.controller.api;

import com.firesoul.collisiontest.model.api.Bar;
import com.firesoul.collisiontest.model.api.Drawable;
import com.firesoul.collisiontest.view.api.Renderable;

public interface DrawableLoader {

	Renderable getFromDrawable(Drawable drawable);

	Drawable loadSpriteFromSystem(String path);

	Bar loadStaticBar(int width, int height, int rgba);
}
