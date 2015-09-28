/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.astedt.robin.walkingishard.plot;

import com.astedt.robin.walkingishard.Config;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.Timer;

/**
 *
 * @author Robin
 */
public class Plot extends JFrame {
    private List<Double>[] data;
    private String[] labels;
    private Color[] colors;
    private PlotDrawingComponent dc;
    private double maxValue;
    
    public Plot(String[] labels, Color[] colors) {
        maxValue = 0.0;
        this.labels = labels;
        this.colors = colors;
        data = new List[labels.length];
        for (int i = 0; i < labels.length; i++) {
            data[i] = new ArrayList<>();
        }
        dc = new PlotDrawingComponent(data, labels, colors);
        setTitle("Walking is hard: Plot");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dc.setPreferredSize(new Dimension(Config.WIDTH, Config.HEIGHT));
        add(dc);
        setResizable(true);
        pack();
        setVisible(true);
        Timer repaintTimer = new Timer(1000, (ActionEvent ae) -> {
            repaint();
        });
        repaintTimer.setRepeats(true);
        repaintTimer.start();
    }
    
    public void addValue(int dataType, double value) {
        data[dataType].add(value);
        if (value > maxValue) {
            maxValue = value;
            dc.setMaxValue(value);
        }
    }
}
