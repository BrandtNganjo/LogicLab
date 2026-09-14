package com.github.BrandtNganjo.LogicLab.logic;

public enum LogicState {
    LOW,
    HIGH,
    UNKNOWN;

    public boolean isKnown() {
        return this != UNKNOWN;
    }

    public boolean asBoolean() {
        if (this == UNKNOWN) {
            throw new IllegalStateException("Cannot convert \"UNKNOWN\" to boolean");
        } ;
        return this == HIGH;
    }

    public static LogicState fromBoolean(boolean value) {
        return value ? HIGH : LOW;
    }
}
