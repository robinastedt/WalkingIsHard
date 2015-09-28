/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.astedt.robin.walkingishard.plot;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;
import javax.swing.JComponent;

/**
 *
 * @author Robin
 */
public class PlotDrawingComponent extends JComponent {
    private List<Double>[] data;
    private String[] labels;
    private Color[] colors;
    private double maxValue;
    private Font font;
    private double xPadding = 0.05;
    private double yPadding = 0.05;
    
    public PlotDrawingComponent(List<Double>[] data, String[] labels, Color[] colors) {
        this.data = data;
        this.labels = labels;
        this.colors = colors;
        maxValue = 0.0;
        font = new Font("Courier New", Font.PLAIN, 14);
    }
    
    public void setMaxValue(double value) {
        maxValue = value;
    }
    
    @Override
    public void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D)graphics;
        g.setColor(Color.black);
        g.fillRect(0, 0, getWidth(), getHeight());
        
        g.setFont(font);
        
        for (int i = 0; i < data.length; i++) {
            g.setColor(colors[i]);
            int points = data[0].size();
            double x0 = xPadding;
            double y0 = yPadding;
            for (int point = 0; point < points; point++) {
                double x1 = xPadding + ((double)(point+1) / points * (1.0 - xPadding * 2.0));
                double y1 = yPadding + (data[i].get(point) / maxValue * (1.0 - yPadding * 2.0));
                
                int x0Draw = (int)(x0 * getWidth());
                int y0Draw = (int)((1.0 - y0) * getHeight());
                int x1Draw = (int)(x1 * getWidth());
                int y1Draw = (int)((1.0 - y1) * getHeight());
                
                g.drawLine(x0Draw, y0Draw, x1Draw, y1Draw);
                
                x0 = x1;
                y0 = y1;
            }
            
            g.drawString(labels[i], font.getSize(), (i+2) * font.getSize());
        }
    }
}
