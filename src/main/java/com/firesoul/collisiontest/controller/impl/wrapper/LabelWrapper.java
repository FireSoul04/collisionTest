package com.firesoul.collisiontest.controller.impl.wrapper;

import com.firesoul.collisiontest.model.impl.drawable.ui.Label;
import com.firesoul.collisiontest.view.impl.renderables.SwingLabel;

import java.awt.*;

public class LabelWrapper extends AbstractRenderableWrapper {

	public LabelWrapper(final Label label) {
		super(label, new SwingLabel(
			new Point((int) label.getPosition().x(), (int) label.getPosition().y()),
			label.getText(), 0.0, label.isVisible())
		);
	}
}
