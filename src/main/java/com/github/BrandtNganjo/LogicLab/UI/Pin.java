package com.github.BrandtNganjo.LogicLab.UI;

public class Pin {
    public static final int RADIUS = 6;
    public int x;
    public int y;
    public boolean isOutput;
    public Node parent;

    public Pin(boolean isOutput, Node parent) {
        this.isOutput = isOutput;
        this.parent = parent;
    }

    public boolean contains(int px, int py) {
        int Deltax = px - x;
        int Deltay = py - y;
        // In a circle, x^2 + y^2 = r^2
        // Get the difference of the pin's position from the mouse and compare that to the raidus
        return (Deltax * Deltax) + (Deltay * Deltay) <= (RADIUS * RADIUS);
    }
}
