package com.github.BrandtNganjo.LogicLab.UI;

import java.awt.*;

public class GateNode extends Node {
    public String label;

    public GateNode(int x, int y, int numInputs, int numOutputs, String label) {
        super(x, y);
        this.label = label;
        this.width = 80;

        int maxPins = Math.max(numInputs, numOutputs);
        this.height = Math.max(40, maxPins * 25 + 15);

        for(int i = 0; i < numInputs; i++) {
            inputs.add(new Pin(false, this));
        }
        for(int i = 0; i < numOutputs; i++) {
            outputs.add(new Pin(true, this));
        }
        updatePins();
    }

    @Override
    public void updatePins() {
        for(int i = 0; i < inputs.size(); i++) {
            inputs.get(i).x = this.x;
            double ratio = (double) (i + 1) / (inputs.size() + 1);
            inputs.get(i).y = this.y + (int) (this.height * ratio);
        }
        for(int i = 0; i < outputs.size(); i++) {
            outputs.get(i).x = this.x + this.width;
            double ratio = (double) (i + 1) / (outputs.size() + 1);
            outputs.get(i).y = this.y + (int) (this.height * ratio);
        }
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(new Color(75, 45, 95));
        g2d.fillRect(x, y, this.width, this.height);

        g2d.setColor(Color.BLACK);
        g2d.drawRect(x, y, this.width, this.height);

        FontMetrics fm = g2d.getFontMetrics();
        int textX = x + (width - fm.stringWidth(label)) / 2;
        int textY = y + (height - fm.getHeight()) / 2 + fm.getAscent();
        g2d.drawString(label, textX, textY);
    }

    @Override
    public void drawPins(Graphics2D g2d) {
        g2d.setColor(Color.BLUE);
        for (Pin p : inputs) {
            g2d.fillOval(p.x - Pin.RADIUS, p.y - Pin.RADIUS, Pin.RADIUS * 2, Pin.RADIUS * 2);
        }
        g2d.setColor(Color.RED);
        for (Pin p : outputs) {
            g2d.fillOval(p.x - Pin.RADIUS, p.y - Pin.RADIUS, Pin.RADIUS * 2, Pin.RADIUS * 2);
        }
    }

}
