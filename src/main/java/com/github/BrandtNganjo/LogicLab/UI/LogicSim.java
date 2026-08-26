package com.github.BrandtNganjo.LogicLab.UI;

import javax.swing.*;

public class LogicSim extends JFrame {
    public LogicSim(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        add(new GraphPanel());
        setVisible(true);
    }

}
