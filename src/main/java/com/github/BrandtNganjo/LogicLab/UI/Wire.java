package com.github.BrandtNganjo.LogicLab.UI;

public class Wire {
    public Pin start;
    public Pin end;

    public Wire(Pin start, Pin end) {
        this.start = start;
        this.end = end;
    }

    public boolean push() {
        boolean changed = end.setState(start.getState());
        if(changed) {
            end.parent.markForUpdate();
        }
        return changed;
    }
}
