package com.github.BrandtNganjo.LogicLab.UI;

import com.github.BrandtNganjo.LogicLab.logic.LogicState;

import java.awt.*;
public class IONode extends Node{
    // True if this Node provides a signal TO the circuit
    // False if Node reads a signal FROM the circuit
    public final boolean isInput;
    private LogicState state = LogicState.UNKNOWN;

    public IONode(int x, int y, boolean isInput) {
        super(x, y, isInput ? "Input" : "Output");
        this.isInput = isInput;
        this.width = 40;
        this.height = 40;

        if(isInput) {
            outputs.add(new Pin(true, this, "OUT"));
        } else {
            inputs.add(new Pin(false, this, "IN"));
        }
        updatePins();
    }

    public boolean isInput() {
        return isInput;
    }

    public LogicState getState() {
        return state;
    }

    public void setState(LogicState state) {
        if(!isInput) {
            return;
        }

        this.state = state;
        outputs.get(0).transmit(state);
        markForUpdate();
    }

    public void toggleState() {
        if(!isInput) {
            return;
        }

        switch(state) {
            case LOW:
                setState(LogicState.HIGH);
                break;
            case HIGH:
                setState(LogicState.LOW);
                break;
            case UNKNOWN:
                setState(LogicState.HIGH);
        };
    }

    @Override
    public void evaluate() {
        if(isInput) {
            outputs.get(0).transmit(state);
        } else {
            state = inputs.get(0).getState();
        }
    }

    @Override
    public void updatePins() {
        Pin p = isInput ? outputs.get(0) : inputs.get(0);
        p.x = isInput ? x + width : x;
        p.y = y + height / 2;
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(isInput ? Color.MAGENTA : Color.ORANGE);
        g2d.fillRoundRect(x, y, width, height, 12, 12);
        g2d.setColor(Color.WHITE);
        g2d.drawRoundRect(x, y, this.width, this.height, 12, 12);

        FontMetrics fm = g2d.getFontMetrics();
        String label = getName();
        int textX = x + (width - fm.stringWidth(label)) / 2;
        int textY = y + (height - fm.getHeight()) / 2 + fm.getAscent();
        g2d.drawString(label, textX, textY);
    }

    private String stateToString (LogicState state) {
        return switch (state) {
            case LOW -> "0";
            case HIGH -> "1";
            case UNKNOWN -> "X";
        };
    }
}