/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.astedt.robin.walkingishard.render;

import com.astedt.robin.walkingishard.Config;
import com.astedt.robin.walkingishard.Main;
import com.astedt.robin.walkingishard.walker.Joint;
import com.astedt.robin.walkingishard.walker.Muscle;
import com.astedt.robin.walkingishard.walker.Walker;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JComponent;

/**
 *
 * @author robin
 */
public class DrawingComponent extends JComponent {

    private final Font fontStats;
    private Main main;
    private Config config;
    
    private double xScroll;
    public double xScrollOffset = 0.0;
    
    public DrawingComponent(Main main, Config config) {
        this.main = main;
        this.config = config;
        xScroll = config.WALKER_SPAWN_X;
        fontStats = new Font("Courier New", Font.PLAIN, 14);
    }
    
    @Override
    public void paintComponent(Graphics graphics) {
        synchronized (main) {
            if (main.running && main.render) {
                Graphics2D g = (Graphics2D) graphics;
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, getWidth(), getHeight());

                Walker walker = main.walkers.get(main.activeWalkerIndex);

                double xTotal = 0.0;

                int scale = Math.min(getWidth(), getHeight()) / 2;

                g.setFont(fontStats);
                
                g.setColor(Color.GRAY);
                int segmentLineWidth = (int)(scale * 0.025);
                int segmentLineHeight = (int)(scale * 0.25);
                int segmentLineDrawX = scale 
                        + (int)(scale * (Math.ceil(xScroll - 1.0) - xScroll)) 
                        - segmentLineWidth / 2;
                g.fillRect(segmentLineDrawX, 0, segmentLineWidth, segmentLineHeight);
                g.drawString(Integer.toString((int)Math.ceil(xScroll - 1.0 - config.WALKER_SPAWN_X)), segmentLineDrawX, segmentLineHeight+fontStats.getSize());

                g.setColor(Color.GRAY);
                int segment2LineWidth = (int)(scale * 0.025);
                int segment2LineHeight = (int)(scale * 0.25);
                int segment2LineDrawX = scale 
                        + (int)(scale * (Math.ceil(xScroll) - xScroll)) 
                        - segment2LineWidth / 2;
                g.fillRect(segment2LineDrawX, 0, segment2LineWidth, segment2LineHeight);
                g.drawString(Integer.toString((int)Math.ceil(xScroll + 0.0 - config.WALKER_SPAWN_X)), segment2LineDrawX, segment2LineHeight+fontStats.getSize());

                g.setColor(Color.GRAY);
                int segment3LineWidth = (int)(scale * 0.025);
                int segment3LineHeight = (int)(scale * 0.25);
                int segment3LineDrawX = scale 
                        + (int)(scale * (Math.ceil(xScroll + 1.0) - xScroll)) 
                        - segment3LineWidth / 2;
                g.fillRect(segment3LineDrawX, 0, segment3LineWidth, segment3LineHeight);
                g.drawString(Integer.toString((int)Math.ceil(xScroll + 1.0 - config.WALKER_SPAWN_X)), segment3LineDrawX, segment3LineHeight+fontStats.getSize());

                g.setColor(Color.GRAY);
                int segment4LineWidth = (int)(scale * 0.025);
                int segment4LineHeight = (int)(scale * 0.25);
                int segment4LineDrawX = scale 
                        + (int)(scale * (Math.ceil(xScroll + 2.0) - xScroll)) 
                        - segment4LineWidth / 2;
                g.fillRect(segment4LineDrawX, 0, segment4LineWidth, segment4LineHeight);
                g.drawString(Integer.toString((int)Math.ceil(xScroll + 2.0 - config.WALKER_SPAWN_X)), segment4LineDrawX, segment4LineHeight+fontStats.getSize());

                g.setColor(Color.WHITE);
                int startLineWidth = (int)(scale * 0.05);
                int startLineHeight = (int)(scale * 0.2);
                int startLineDrawX = scale 
                        + (int)(scale * (config.WALKER_SPAWN_X - xScroll)) 
                        - startLineWidth / 2;
                g.fillRect(startLineDrawX, 0, startLineWidth, startLineHeight);

                g.setColor(Color.RED);
                int travelledMaxLineWidth = (int)(scale * 0.05);
                int travelledMaxLineHeight = (int)(scale * 0.15);
                int travelledMaxLineDrawX = scale 
                        + (int)(scale * (config.WALKER_SPAWN_X + walker.travelledMax - xScroll)) 
                        - travelledMaxLineWidth / 2;
                g.fillRect(travelledMaxLineDrawX, 0, travelledMaxLineWidth, travelledMaxLineHeight);
                
                g.setColor(Color.BLUE);
                int positionLineWidth = (int)(scale * 0.0125);
                int positionLineHeight = (int)(scale * 0.1);
                int positionLineDrawX = scale 
                        + (int)(scale * (-xScrollOffset)) 
                        - positionLineWidth / 2;
                g.fillRect(positionLineDrawX, 0, positionLineWidth, positionLineHeight);
                
                g.setColor(Color.GREEN);
                int bestPositionLineWidth = (int)(scale * 0.05);
                int bestPositionLineHeight = (int)(scale * 0.15);
                int bestPositionLineDrawX = scale 
                        + (int)(scale * (config.WALKER_SPAWN_X + main.distanceRecord - xScroll)) 
                        - bestPositionLineWidth / 2;
                g.fillRect(bestPositionLineDrawX, 0, bestPositionLineWidth, bestPositionLineHeight);



                for (Joint joint : walker.joints) {
                    xTotal += joint.x;

                    if (joint.id == 0) g.setColor(Color.green);
                    else g.setColor(Color.white);


                    int xDraw = scale + (int)(scale * (joint.x - xScroll));
                    int yDraw = scale + (int)(scale * joint.y);

                    int jointRadius = (int)(scale * joint.r);
                    int jointDiameter = jointRadius + jointRadius;

                    g.drawOval(xDraw - jointRadius, yDraw - jointRadius, jointDiameter, jointDiameter);

                }
                g.setColor(Color.white);
                for (Muscle muscle : walker.muscles) {
                    int xDraw = scale + (int)(scale * (muscle.joint1.x - xScroll));
                    int yDraw = scale + (int)(scale * muscle.joint1.y);
                    int xDraw2 = scale +(int)(scale * (muscle.joint2.x - xScroll));
                    int yDraw2 = scale + (int)(scale * muscle.joint2.y);
                    g.drawLine(xDraw, yDraw, xDraw2, yDraw2);
                }

                //Draw world

                g.setColor(Color.white);
                double wl = main.world.getWavelength(xScroll);
                double extraWidth = 2.0 * (getWidth() - getHeight()) / getHeight();
                if (extraWidth < 0.0) extraWidth = 0.0;
                for (double x1 = xScroll - 1.0; x1 <= xScroll + 1.0 + extraWidth; x1 += wl) {
                    double x2 = x1 + wl;
                    double y1 = main.world.getPoint(x1);
                    double y2 = main.world.getPoint(x2);

                    g.drawLine(scale + (int)(scale * (x1-xScroll)), scale + (int)(scale * y1), scale + (int)(scale * (x2 - xScroll)), scale + (int)(scale * y2));
                }

                xScroll = xTotal / walker.joints.size() + xScrollOffset;
                
                g.setColor(Color.white);
                g.setFont(fontStats);
                
                g.drawString("Generation: " 
                        + String.format("%5d", main.generation+1) 
                        + ":     Walker: "
                        + String.format("%4d", main.activeWalkerIndex+1)
                        + "/"
                        + String.format("%4d", main.walkers.size()), 
                        10, getHeight() - fontStats.getSize() * 6);
                g.drawString("Current walker's best distance:   " 
                        + String.format("%.4f", main.walkers.get(main.activeWalkerIndex).travelledMax), 
                        10, getHeight() - fontStats.getSize() * 5);
                g.drawString("Average distance this generation: " 
                        + String.format("%.4f", main.distanceAverageGeneration), 
                        10, getHeight() - fontStats.getSize() * 4);
                g.drawString("Best average distance ever:       " 
                        + String.format("%.4f", main.distanceAverageGenerationRecord), 
                        10, getHeight() - fontStats.getSize() * 3);
                g.drawString("Best distance this generation:    " 
                        + String.format("%.4f", main.distanceRecordGeneration), 
                        10, getHeight() - fontStats.getSize() * 2);
                g.drawString("Best distance ever:               " 
                        + String.format("%.4f", main.distanceRecord), 
                        10, getHeight() - fontStats.getSize() * 1);
            }
        }
        
        
    }
}
