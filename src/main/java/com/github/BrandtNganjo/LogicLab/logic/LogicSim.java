package com.github.BrandtNganjo.LogicLab.logic;

import java.util.Queue;
import java.util.HashSet;
import java.util.ArrayDeque;
import java.util.Set;

import com.github.BrandtNganjo.LogicLab.UI.Node;
import com.github.BrandtNganjo.LogicLab.UI.Wire;

public class LogicSim {

    public enum SimulationStatus{
        STABLE,
        UNSTABLE
    }

    private static final int MAX_DELTA_CYCLES = 1000;

    private final Queue<Node> updateQueue = new ArrayDeque<>();
    private final Circuit circuit;
    private SimulationStatus status = SimulationStatus.STABLE;

    private boolean evaluating;

    public LogicSim(Circuit circuit) {
        if(circuit == null) {
            throw new IllegalArgumentException("Circuit cannot be null");
        }
        this.circuit = circuit;
    }

    public SimulationStatus getStatus() {
        return status;
    }

    public boolean isEvaluating() {
        return evaluating;
    }

    public boolean isStable() {
        return status == SimulationStatus.STABLE;
    }

    public boolean isUnstable() {
        return status == SimulationStatus.UNSTABLE;
    }

    public void queueNode(Node node) {
        if(node != null && !updateQueue.contains(node)) {
            updateQueue.add(node);
        }
    }

    public void evaluateAll() {
        if (evaluating) return;

        updateQueue.clear();

        for(Node node : circuit.getNodes()) {
            queueNode(node);
        }

        processQueue();
    }

    public void processQueue() {
        if (evaluating) return;

        evaluating = true;
        status = SimulationStatus.STABLE;

        try {
            int deltaCycles = 0;

            while(!updateQueue.isEmpty()) {
               deltaCycles++;

               if(deltaCycles > MAX_DELTA_CYCLES) {
                   status = SimulationStatus.UNSTABLE;
                   updateQueue.clear();
                   break;
               }

               Node node = updateQueue.poll();

               node.evaluate();

               for(Wire wire : circuit.getWires()) {
                   if(wire.start.parent == node) {
                       wire.push();
                   }
                }
            }
        } finally {
            evaluating = false;
        }
    }
}