/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.astedt.robin.walkingishard.plot;

import com.astedt.robin.walkingishard.Config;
import com.astedt.robin.walkingishard.Main;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.Timer;

/**
 *
 * @author Robin
 */
public class Plot implements Serializable {
    
    private transient JFrame window;
    private Config config;
    private List<Double>[] data;
    private String[] labels;
    private Color[] colors;
    private transient PlotDrawingComponent dc;
    private double maxValue;
    
    public Plot(Config config, String[] labels, Color[] colors) {
        this.config = config;
        maxValue = 0.0;
        this.labels = labels;
        this.colors = colors;
        data = new List[labels.length];
        for (int i = 0; i < labels.length; i++) {
            data[i] = new ArrayList<>();
        }
    }
    
    public void createWindow() {
        window = new JFrame();
        dc = new PlotDrawingComponent(data, labels, colors);
        window.setTitle("Walking is hard: Plot");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dc.setPreferredSize(new Dimension(config.WIDTH, config.HEIGHT));
        window.add(dc);
        window.setResizable(true);
        window.pack();
        window.setVisible(true);
        Timer repaintTimer = new Timer(1000, (ActionEvent ae) -> {
            window.repaint();
        });
        repaintTimer.setRepeats(true);
        repaintTimer.start();
        dc.setMaxValue(maxValue);
    }
    
    public void addValue(int dataType, double value) {
        data[dataType].add(value);
        if (value > maxValue) {
            maxValue = value;
            if (dc != null) dc.setMaxValue(value);
        }
    }
}
