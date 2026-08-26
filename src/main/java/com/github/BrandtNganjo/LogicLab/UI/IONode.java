package com.github.BrandtNganjo.LogicLab.UI;

import java.awt.*;
public class IONode extends Node{
    // True if this Node provides a signal TO the circuit
    // False if Node reads a signal FROM the circuit
    public boolean isInput;

    public IONode(int x, int y, boolean isInput) {
        super(x, y);
        this.isInput = isInput;
        this.width = 40;
        this.height = 40;

        if(isInput) {
            outputs.add(new Pin(true, this));
        } else {
            inputs.add(new Pin(false, this));
        }
        updatePins();
    }

    @Override
    public void updatePins() {
        if(!outputs.isEmpty()) {
            outputs.get(0).x = this.x + this.width / 2;
            outputs.get(0).y = this.y + this.height / 2;
        }
        if(!inputs.isEmpty()) {
            inputs.get(0).x = this.x + this.width / 2;
            inputs.get(0).y = this.y + this.height / 2;
        }
    }

    @Override
    public void draw(Graphics2D g2d) {
        if(isInput) {
            g2d.setColor(Color.MAGENTA);
        } else {
            g2d.setColor(Color.ORANGE);
        }
        g2d.fillRect(x, y, width, height);
        g2d.setColor(Color.WHITE);
        g2d.drawRect(x, y, this.width, this.height);
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