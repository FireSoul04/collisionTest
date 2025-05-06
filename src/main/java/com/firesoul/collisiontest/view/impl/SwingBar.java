package com.firesoul.collisiontest.view.impl;

import com.firesoul.collisiontest.model.util.Vector2;
import com.firesoul.collisiontest.view.api.Bar;

import java.awt.*;

public class SwingBar extends SwingDrawable implements Bar {

    private final boolean staticc;
    private final Color color;
    private double currentPercentage;

    public SwingBar(final int width, final int height, final Color color, final boolean visible, final boolean staticc) {
        super(Vector2.zero(), visible);
        this.setSize(width, height);
        this.color = color;
        this.currentPercentage = 1.0;
        this.staticc = staticc;
    }

    @Override
    public boolean isStatic() {
        return this.staticc;
    }

    @Override
    public void setCurrentPercentage(final double currentPercentage) {
        this.currentPercentage = currentPercentage;
    }

    @Override
    public void drawComponent(final Graphics g) {
        if (this.isVisible()) {
            final int currentWidth = (int) (this.getWidth() * currentPercentage);
            final Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.BLACK);
            g2.fillRect((int) this.getPosition().x(), (int) this.getPosition().y(), this.getWidth(), this.getHeight());
            g2.setColor(this.color);
            g2.fillRect((int) this.getPosition().x() + 1, (int) this.getPosition().y() + 1, currentWidth - 2, this.getHeight() - 2);
        }
    }
}
