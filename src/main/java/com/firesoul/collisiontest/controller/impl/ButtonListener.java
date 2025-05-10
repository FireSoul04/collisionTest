package com.firesoul.collisiontest.controller.impl;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class ButtonListener {

	private static class Button {
		private boolean clicked = false;
		public Button(final JButton button) {
			button.addActionListener(e -> clicked = true);
		}
		public boolean isClicked() {
			return clicked;
		}
	}

	private final Map<String, Button> buttons = new HashMap<>();

	public void addButton(final String name, final JButton button) {
		this.buttons.put(name, new Button(button));
	}

	public boolean isButtonClicked(final String name) {
		return this.buttons.get(name).isClicked();
	}
}
