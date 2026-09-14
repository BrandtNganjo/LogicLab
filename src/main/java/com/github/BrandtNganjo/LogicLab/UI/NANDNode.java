package com.github.BrandtNganjo.LogicLab.UI;

import com.github.BrandtNganjo.LogicLab.logic.LogicState;

import java.awt.*;

public class NANDNode extends Node{
    public final Pin inputA;
    public final Pin inputB;
    public final Pin output;

    public NANDNode(int x, int y) {
        super(x, y, "NAND");
        this.width = 80;
        this.height = 40;
        this.inputA = new Pin(false, this, "A");
        this.inputB = new Pin(false, this, "B");
        this.output = new Pin(true, this, "OUT");
        inputs.add(inputA);
        inputs.add(inputB);
        outputs.add(output);
        updatePins();
    }

    @Override
    public void evaluate() {
        LogicState a = inputA.getState();
        LogicState b = inputB.getState();
        LogicState result;

        if(a == LogicState.LOW || b == LogicState.LOW) {
            result = LogicState.HIGH;
            System.out.println("High: a: " + a + " b: " + b);
        } else if(a == LogicState.HIGH
                && b == LogicState.HIGH) {
            result = LogicState.LOW;
            System.out.println("Low: a: " + a + " b: " + b);
        } else {
            result = LogicState.UNKNOWN;
            System.out.println("Unknown: a: " + a + " b: " + b);
        }
        output.transmit(result);
    }

    @Override
    public void updatePins() {
        inputA.x = x;
        inputA.y = y + height / 3;

        inputB.x = x;
        inputB.y = y + (2* height) / 3;

        output.x = x + width;
        output.y = y + height / 2;
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(new Color(75, 45, 95));
        g2d.fillRoundRect(x, y, this.width, this.height, 12, 12);

        g2d.setColor(Color.WHITE);
        g2d.drawRoundRect(x, y, this.width, this.height, 12, 12);

        FontMetrics fm = g2d.getFontMetrics();
        String label = getName();
        int textX = x + (width - fm.stringWidth(label)) / 2;
        int textY = y + (height - fm.getHeight()) / 2 + fm.getAscent();
        g2d.drawString(label, textX, textY);
    }
}
