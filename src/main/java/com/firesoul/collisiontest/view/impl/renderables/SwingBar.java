package com.firesoul.collisiontest.view.impl.renderables;

import com.firesoul.collisiontest.view.api.RenderableBar;
import com.firesoul.collisiontest.view.impl.renderables.shapes.SwingRectangle;

import java.awt.*;

public class SwingBar extends SwingRenderable implements RenderableBar {

    private final SwingRenderable base;
    private final SwingRenderable loading;

    private double currentPercentage;

    public SwingBar(final int width, final int height, final Color color, final boolean visible) {
        super(new Point(), visible);
        this.resize(width, height);
        this.base = new SwingRectangle(new Point(), width, height, Color.BLACK, visible);
        this.loading = new SwingRectangle(new Point(), width - 2, height - 2, color, visible);
        this.currentPercentage = 1.0;
    }

    @Override
    public void setCurrentPercentage(final double currentPercentage) {
        this.currentPercentage = currentPercentage;
    }

    @Override
    public void drawComponent(final Graphics g) {
        // if (this.isVisible()) {
            final Point start = new Point(this.getPosition().x + 1, this.getPosition().y + 1);
            final int currentWidth = (int) ((this.getWidth() - 2) * currentPercentage);
            final Graphics2D g2 = (Graphics2D) g;
            this.base.translate(this.getPosition());
            this.base.drawComponent(g2);
            this.loading.translate(start);
            this.loading.resize(currentWidth, this.getHeight() - 2);
            this.loading.drawComponent(g2);
        // }
    }
}
