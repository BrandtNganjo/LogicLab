package com.github.BrandtNganjo.LogicLab.UI;

import com.github.BrandtNganjo.LogicLab.logic.Circuit;
import com.github.BrandtNganjo.LogicLab.logic.LogicSim;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;


public class GraphPanel extends JPanel {

    public final Circuit circuit;
    public final LogicSim simulator;

    private Node draggedNode;

    private int dragOffsetX;
    private int dragOffsetY;

    private Pin wiringFrom;
    private Pin hoveredPin;

    private int mouseX;
    private int mouseY;


    public GraphPanel() {
        setBackground(new Color(30, 30, 30));
        this.circuit = new Circuit();
        this.simulator = new LogicSim(circuit);

        IONode a = new IONode(80, 150, true);
        IONode b = new IONode(80, 300, true);
        NANDNode nand = new NANDNode(300, 210);
        NANDNode nand2 = new NANDNode(300, 410);
        IONode output = new IONode(550,235,false);
        addNode(a);
        addNode(b);
        addNode(nand);
        addNode(nand2);
        addNode(output);
        //add and connect nodes
        simulator.evaluateAll();
        initiateMouseListeners();
    }

    public void addNode(Node node) {
        node.setSimulator(simulator);
        circuit.addNode(node);
    }

    private void initiateMouseListeners() {

        MouseAdapter ma = new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {

                mouseX = e.getX();
                mouseY = e.getY();

                if (SwingUtilities.isRightMouseButton(e)) {
                    handleRightClick(e.getX(), e.getY());
                    return;
                }

                if (SwingUtilities.isLeftMouseButton(e)) {

                    Pin pin = getPinAt(e.getX(), e.getY());

                    if (pin != null) {

                        if (pin.isOutput) {
                            wiringFrom = pin;
                            hoveredPin = null;
                            repaint();
                        }

                        return;
                    }

                    for(int i = circuit.getNodes().size() - 1; i >= 0; i--) {

                        Node n = circuit.getNodes().get(i);

                        if (n.contains(e.getX(), e.getY())) {

                            draggedNode = n;

                            dragOffsetX = e.getX() - n.x;
                            dragOffsetY = e.getY() - n.y;

                            circuit.bringNodeToFront(n);

                            repaint();
                            return;
                        }
                    }
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {

                mouseX = e.getX();
                mouseY = e.getY();

                if(wiringFrom != null) {
                    hoveredPin = findInputAt(e.getX(), e.getY());
                    repaint();
                    return;
                }

                if(draggedNode != null) {

                    draggedNode.x = e.getX() - dragOffsetX;
                    draggedNode.y = e.getY() - dragOffsetY;

                    draggedNode.updatePins();

                    repaint();
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {

                mouseX = e.getX();
                mouseY = e.getY();

                if(wiringFrom != null) {
                    hoveredPin = findInputAt(e.getX(), e.getY());
                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {

                mouseX = e.getX();
                mouseY = e.getY();

                if(wiringFrom != null) {

                    Pin input = findInputAt(e.getX(), e.getY());

                    if (input != null) {
                        connect(wiringFrom, input);
                    }

                    wiringFrom = null;
                    hoveredPin = null;

                    repaint();
                }

                draggedNode = null;
            }
        };

        addMouseListener(ma);
        addMouseMotionListener(ma);
    }

    private Pin getPinAt(int x, int y) {

        for(int i = circuit.getNodes().size() - 1; i >= 0; i--) {

            Pin pin = circuit.getNodes().get(i).getPinAt(x, y);

            if(pin != null) {
                return pin;
            }
        }

        return null;
    }

    private Pin findInputAt(int x, int y) {

        for(Node node : circuit.getNodes()) {

            for(Pin pin : node.inputs) {

                if(pin.contains(x, y)) {
                    return pin;
                }
            }
        }

        return null;
    }

    private void connect(Pin output, Pin input) {
        try {
            circuit.connect(output, input);
            simulator.processQueue();
        } catch(IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        repaint();
    }

    private void handleRightClick(int x, int y) {

        Pin pin = getPinAt(x, y);

        if(pin != null) {
            if (pin.isOutput
                    && pin.parent instanceof IONode io
                    && io.isInput()) {
                io.toggleState();
                simulator.processQueue();
                repaint();
                return;
            }

            circuit.removeConnectionsAt(pin);
            simulator.processQueue();
            repaint();
            return;
        }

        Wire wire = findWireAt(x,y);

        if(wire != null) {
            circuit.removeWire(wire);
            simulator.processQueue();
            repaint();
        }
    }

    private Wire findWireAt(int x, int y) {

        for(Wire wire : circuit.getWires()) {

            if(distanceToLineSegment(
                    x,
                    y,
                    wire.start.x,
                    wire.start.y,
                    wire.end.x,
                    wire.end.y
            ) <= 7) {
                return wire;
            }
        }

        return null;
    }

    private double distanceToLineSegment(
            double px,
            double py,
            double x1,
            double y1,
            double x2,
            double y2
    ) {

        double dx = x2 - x1;
        double dy = y2 - y1;

        if(dx == 0 && dy == 0) {
            return Math.hypot(px - x1, py - y1);
        }

        double t =
                ((px - x1) * dx + (py - y1) * dy)
                        / (dx * dx + dy * dy);

        t = Math.clamp(t, 0, 1);

        double closestX = x1 + t * dx;
        double closestY = y1 + t * dy;

        return Math.hypot(px - closestX, py - closestY);
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        // Render all Nodes
        for(Node n : circuit.getNodes()) {
            n.draw(g2d);
        }

        //Render all wires
        g2d.setStroke(new BasicStroke(3));

        for(Wire w : circuit.getWires()) {

            g2d.setColor(Color.LIGHT_GRAY);

            g2d.drawLine(
                    w.start.x,
                    w.start.y,
                    w.end.x,
                    w.end.y
            );
        }

        if(wiringFrom != null) {

            g2d.setColor(Color.YELLOW);

            g2d.drawLine(
                    wiringFrom.x,
                    wiringFrom.y,
                    mouseX,
                    mouseY
            );
        }

        // Draw all pins

        for(Node n : circuit.getNodes()) {
            n.drawPins(g2d);
        }

        if(hoveredPin != null) {

            g2d.setColor(Color.YELLOW);

            g2d.setStroke(new BasicStroke(2));

            g2d.drawOval(
                    hoveredPin.x - Pin.RADIUS - 3,
                    hoveredPin.y - Pin.RADIUS - 3,
                    Pin.RADIUS * 2 + 6,
                    Pin.RADIUS * 2 + 6
            );
        }
    }
}