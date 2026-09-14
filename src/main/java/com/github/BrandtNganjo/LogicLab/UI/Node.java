package com.github.BrandtNganjo.LogicLab.UI;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import  java.util.UUID;
import com.github.BrandtNganjo.LogicLab.logic.LogicSim;

public abstract class Node {
    public int x, y, width, height;

    private final UUID id;
    private String name;

    public List<Pin> inputs = new ArrayList<>();
    public List<Pin> outputs = new ArrayList<>();

    public LogicSim activeSimulator;

    public Node(int x, int y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.id = UUID.randomUUID();
    }

    public UUID getId() { return id; }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSimulator(LogicSim simulator) {
        this.activeSimulator = simulator;
    }

    // Called by an input pin when its state flips
    public void markForUpdate() {
        if(activeSimulator != null) {
            activeSimulator.queueNode(this);
        }
    }

    public boolean contains(int px, int py) {
        return (px >= x)
                && (px <= x + width)
                && (py >= y)
                && (py <= y + height);
    }

    public Pin getPinAt(int px, int py) {
        for (Pin p : inputs) {
            if (p.contains(px, py)) return p;
        }
        for (Pin p : outputs) {
            if (p.contains(px, py)) return p;
        }
        return null;
    }

    public abstract void evaluate();

    public abstract void updatePins();

    public void draw(Graphics2D g2d) {}

    public void drawPins(Graphics2D g2d) {
        for (Pin p : inputs) {
            g2d.setColor(
                    p.isHIGH()
                            ? new Color(96, 204, 108)
                            : new Color(40, 40, 40)
                    );
            g2d.fillOval(
                    p.x - Pin.RADIUS,
                    p.y - Pin.RADIUS,
                    Pin.RADIUS * 2,
                    Pin.RADIUS * 2);
        }
        for (Pin p : outputs) {
            g2d.setColor(
                    p.isHIGH()
                            ? new Color(96, 204, 108)
                            : new Color(40, 40, 40)
            );
            g2d.fillOval(
                    p.x - Pin.RADIUS,
                    p.y - Pin.RADIUS,
                    Pin.RADIUS * 2,
                    Pin.RADIUS * 2);
        }
    }

}
