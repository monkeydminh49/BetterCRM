package com.raven.swing;

import com.raven.model.StatusType;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JLabel;

public class TableStatus extends JLabel {

    public StatusType getType() {
        return type;
    }

    public TableStatus() {
        setForeground(Color.WHITE);
    }

    private StatusType type;

    public void setType(StatusType type) {
        this.type = type;
        setText(type.toString());
        repaint();
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        if (type != null) {
            Graphics2D g2 = (Graphics2D) grphcs;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            GradientPaint g;
            if (type == StatusType.YES) {
                g = new GradientPaint(0, 0, new Color(64, 194, 93), 0, getHeight(), new Color(64, 194, 93));
            } else if (type == StatusType.NO) {
                g = new GradientPaint(0, 0, new Color(231, 208, 88), 0, getHeight(), new Color(217, 191, 79));
            } else {
                g = new GradientPaint(0, 0, new Color(255, 55, 55), 0, getHeight(), new Color(222, 36, 36));
            }
            g2.setPaint(g);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 1, 1);
        }
        super.paintComponent(grphcs);
    }
}
