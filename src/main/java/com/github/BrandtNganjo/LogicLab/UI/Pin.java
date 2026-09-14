package com.github.BrandtNganjo.LogicLab.UI;

import com.github.BrandtNganjo.LogicLab.logic.LogicState;

public class Pin {
    public static final int RADIUS = 6;
    public int x;
    public int y;

    public final boolean isOutput;
    public final Node parent;

    private final String name;
    private LogicState state;

    public Pin(boolean isOutput, Node parent, String name) {
        if(!isOutput) {
            this.state = LogicState.LOW;
        } else {
            this.state = LogicState.UNKNOWN;
        }
        this.isOutput = isOutput;
        this.parent = parent;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public LogicState getState() {
        return state;
    }

    public boolean isHIGH() {
        return state == LogicState.HIGH;
    }

    public boolean isLow() {
        return state == LogicState.LOW;
    }

    public boolean isUnknown() {
        return state == LogicState.UNKNOWN;
    }

    public boolean setState(LogicState newState) {
        if(state == newState) {
            return false;
        }
        state = newState;
        return true;
    }

    public boolean transmit(LogicState newState) {
        return setState(newState);
    }

    public void toggle() {
        if(!isOutput) return;

        state = switch (state) {
            case LOW -> LogicState.HIGH;
            case HIGH -> LogicState.LOW;
            case UNKNOWN -> LogicState.HIGH;
        };

        parent.markForUpdate();
    }

    public boolean contains(int px, int py) {
        int Deltax = px - x;
        int Deltay = py - y;
        // In a circle, x^2 + y^2 = r^2
        // Get the difference of the pin's position from the mouse and compare that to the raidus
        return (Deltax * Deltax) + (Deltay * Deltay) <= (RADIUS * RADIUS);
    }
}
