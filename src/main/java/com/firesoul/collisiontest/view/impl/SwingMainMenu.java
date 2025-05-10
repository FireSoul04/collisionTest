package com.firesoul.collisiontest.view.impl;

import com.firesoul.collisiontest.controller.impl.ButtonListener;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class SwingMainMenu extends JPanel {

	public SwingMainMenu(final ButtonListener listener) {
		super(new BorderLayout(50, 50));
		final JPanel titlePanel = new JPanel();
		final JPanel buttonsPanel = new JPanel(new GridLayout(3, 1, 20, 20));
		final JButton start = new JButton("Start");
		final JButton options = new JButton("Options");
		final JButton exit = new JButton("Exit");
		final Map<String, JButton> buttons = Map.of(
			"Start", start,
			"Options", options,
			"Exit", exit
		);
		titlePanel.add(new JLabel("TITLE"));
		buttonsPanel.add(start);
		buttonsPanel.add(options);
		buttonsPanel.add(exit);
		buttons.forEach(listener::addButton);
		this.add(titlePanel, BorderLayout.NORTH);
		this.add(buttonsPanel, BorderLayout.CENTER);
	}
}
