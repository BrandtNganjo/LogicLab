package com.github.BrandtNganjo.LogicLab.UI;

import javax.swing.*;

public class Scene extends JFrame {
    public Scene(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        add(new GraphPanel());
        setVisible(true);
    }
}
