package com.github.BrandtNganjo.LogicLab.UI;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
public  class Node {
    public int x, y, width, height;

    public List<Pin> inputs = new ArrayList<>();
    public List<Pin> outputs = new ArrayList<>();

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean contains(int px, int py) {
        return (px >= x) && (px <= x + width) && (py >= y) && (py <= y + height);
    }

    public Pin getPinAt(int px, int py) {
        for(Pin p : inputs) {
            if(p.contains(px, py)) return p;
        }
        for(Pin p : outputs) {
            if(p.contains(px, py)) return p;
        }
        return null;
    }

    public void updatePins() {}

    public void draw(Graphics2D g2d) {}

    public void drawPins(Graphics2D g2d) {}

}
