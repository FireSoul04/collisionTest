package com.firesoul.collisiontest.controller.impl.wrapper;

import com.firesoul.collisiontest.model.api.gameobjects.Camera;
import com.firesoul.collisiontest.model.impl.drawable.ui.Label;
import com.firesoul.collisiontest.view.api.Renderable;
import com.firesoul.collisiontest.view.impl.renderables.SwingLabel;

import java.awt.*;

public class LabelWrapper extends AbstractRenderableWrapper {

	public LabelWrapper(final Label label, final int rgba) {
		super(label, new SwingLabel(
			new Point((int) label.getPosition().x(), (int) label.getPosition().y()),
			label.getText(), 0.0, rgba, label.isVisible())
		);
	}

	@Override
	public Renderable wrap(final Camera camera) {
		((SwingLabel) this.getRenderable()).setText(((Label) this.getDrawable()).getText());
		this.getRenderable().translate(this.getDrawable().getPosition().x(), this.getDrawable().getPosition().y());
		this.getRenderable().setVisible(this.getDrawable().isVisible());
		return this.getRenderable();
	}
}
