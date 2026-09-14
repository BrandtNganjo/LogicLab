package com.github.BrandtNganjo.LogicLab.logic;

import com.github.BrandtNganjo.LogicLab.UI.Node;
import com.github.BrandtNganjo.LogicLab.UI.Pin;
import com.github.BrandtNganjo.LogicLab.UI.Wire;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class Circuit {
    private final List<Node> nodes = new ArrayList<>();
    private final List<Wire> wires = new ArrayList<>();

    public List<Node> getNodes() {
        return Collections.unmodifiableList(nodes);
    }

    public List<Wire> getWires() {
        return Collections.unmodifiableList(wires);
    }

    public void addNode(Node node) {
        if(node == null) {
            throw new IllegalArgumentException( "Node cannot be null");
        }
        if(!nodes.contains(node)) {
            nodes.add(node);
        }
    }

    public void removeNode(Node node) {
        if(node == null) {
            return;
        }

        if(!nodes.remove(node)) {
            return;
        }

        wires.removeIf(wire ->
                wire.start.parent == node
                || wire.end.parent == node);
    }

    public Wire connect(Pin output, Pin input) {
        if(output == null || input == null) {
            throw new IllegalArgumentException("Pins cannot be null");
        }

        if(!output.isOutput || input.isOutput) {
            throw new IllegalArgumentException("A connection must go output -> input");
        }

        if(output.parent == input.parent) {
            throw new IllegalArgumentException("A node cannot connect to itself");
        }

        wires.removeIf(wire -> wire.end == input);
        Wire wire = new Wire(output, input);
        wires.add(wire);
        wire.push();
        return wire;
    }

    public void bringNodeToFront(Node node) {
        if(node != null && nodes.remove(node)) {
            nodes.add(node);
        }
    }

    public void removeWire(Wire wire) {
        if(wire != null && wires.remove(wire)) {
            if(!wire.end.isOutput) {
                wire.end.setState(LogicState.UNKNOWN);
                wire.end.parent.markForUpdate();
            }
        }
    }

    public void removeConnectionsAt(Pin p) {
        if(p.isOutput) {
            wires.removeIf(wire -> wire.start == p);
        } else {
            wires.removeIf(wire -> wire.end == p);
            p.setState(LogicState.UNKNOWN);
        }
    }
}
