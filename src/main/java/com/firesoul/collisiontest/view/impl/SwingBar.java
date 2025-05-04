package com.firesoul.collisiontest.view.impl;

import com.firesoul.collisiontest.model.util.Vector2;
import com.firesoul.collisiontest.view.api.Bar;

import java.awt.*;

public class SwingBar extends SwingDrawable implements Bar {

    private final int maxValue;
    private int currentValue;

    public SwingBar(final Vector2 position, final int width, final int height, final int maxValue, final boolean visible) {
        super(position, visible);
        this.setSize(width, height);
        this.maxValue = maxValue;
        this.currentValue = maxValue;
    }

    @Override
    public void setCurrentValue(final int currentValue) {
        this.currentValue = currentValue;
    }

    @Override
    public void drawComponent(final Graphics g) {
        if (this.isVisible()) {
            final Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.RED);
            final int currentWidth = this.getWidth() * currentValue / maxValue;
            g2.fillRect((int) this.getPosition().x(), (int) this.getPosition().y(), currentWidth, this.getHeight());
            g2.drawRect((int) this.getPosition().x(), (int) this.getPosition().y(), this.getWidth(), this.getHeight());
        }
    }
}
