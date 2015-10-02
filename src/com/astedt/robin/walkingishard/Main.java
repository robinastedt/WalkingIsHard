
package com.astedt.robin.walkingishard;

import com.astedt.robin.walkingishard.genetics.GeneticAlgorithm;
import com.astedt.robin.walkingishard.render.DrawingComponent;
import com.astedt.robin.walkingishard.genetics.Genome;
import com.astedt.robin.walkingishard.plot.Plot;
import com.astedt.robin.walkingishard.walker.Joint;
import com.astedt.robin.walkingishard.walker.Walker;
import com.astedt.robin.walkingishard.world.World;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 *
 * @author robin
 */
public class Main extends JFrame {
    
    
    public boolean running;
    private Random random;
    public List<Walker> walkers;
    public long generation;
    public int activeWalkerIndex;
    public World world;
    public DrawingComponent dc;
    private Plot plot;
    private Config config;
    private long time;
    
    public boolean saveOnExit;
    
    public boolean timeSlow;
    public boolean render;
    
    public double distanceRecord;
    public double distanceRecordGeneration;
    private double distanceTotalGeneration;
    public double distanceAverageGeneration;
    public double distanceAverageGenerationRecord;
    
    
    public static void main(String[] args) {
        
        /*boolean saveOnExit = false;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equalsIgnoreCase("save")) {
                saveOnExit = true;
            }
            System.out.println("Saving on exit: True");
        }
        */
        Config config = new Config("config.properties");
        config.load();
        config.save();
        Main window = new Main(config);
        //window.saveOnExit = saveOnExit;
        window.run();
    }
    
    public Main(Config config) {
        this.config = config;
        init();
        setTitle("Walking is hard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dc = new DrawingComponent(this, config);
        dc.setPreferredSize(new Dimension(config.WIDTH, config.HEIGHT));
        add(dc);
        addKeyListener(new KeyHandler(this, config));
        setResizable(true);
        pack();
        pack();
        setVisible(true);
    }
    
    private void run() {
        
        
        while (running) {
            synchronized (this) {
                step();
            }
            if (render) repaint();
            if (timeSlow) {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            
        }
        
        dispose();
    }
    
    private void init() {
        
        random = new Random();
        time = 0;
        timeSlow = true;
        render = true;
        world = new World(config, random.nextLong());
        activeWalkerIndex = 0;
        generation = 0;
        distanceRecord = 0.0;
        distanceRecordGeneration = 0.0;
        distanceTotalGeneration = 0.0;
        distanceAverageGeneration = 0.0;
        distanceAverageGenerationRecord = 0.0;
        
        plot = new Plot(config,
        new String[] {
            "Distance record",
            "Distance record this generation",
            "Distance average record",
            "Distance average this generation"
        },
        new Color[] {
            Color.RED.darker().darker(),
            Color.RED,
            Color.GREEN.darker().darker(),
            Color.GREEN
        });
        
        //distanceRecordList = new ArrayList<>();
        //distanceRecordGenerationList = new ArrayList<>();
        //distanceAverageGenerationList = new ArrayList<>();
        //distanceAverageGenerationRecordList = new ArrayList<>();
        walkers = new ArrayList<>();
        
        for (int i = 0; i < config.POPULATION_SIZE; i++) {
            walkers.add(new Walker(config, random));
        }
        
        running = true;
    }
    
    private void step() {
        
        
        walkers.get(activeWalkerIndex).step(time, world);
        time++;
        
        
        
        if (walkers.get(activeWalkerIndex).alive == false
                || time >= walkers.get(activeWalkerIndex).lastDistanceRecordTime + config.EVAL_TIME_OUT) {
            Walker lastWalker = walkers.get(activeWalkerIndex);
            
            if (lastWalker.travelledMax > distanceRecord) distanceRecord = lastWalker.travelledMax;
            if (lastWalker.travelledMax > distanceRecordGeneration) distanceRecordGeneration = lastWalker.travelledMax;
            
            double recordRatio;
            if (distanceRecord == 0.0) recordRatio = 0.0;
            else recordRatio = lastWalker.travelledMax / distanceRecord;
            
            distanceTotalGeneration += lastWalker.travelledMax;
            /*
            System.out.println(
                    "Walker " + String.format("%3d", activeWalkerIndex) 
                    + " (j" + String.format("%3d", lastWalker.joints.size())
                    + ", m" + String.format("%3d", lastWalker.muscles.size()) + "): "
                    + "steps: " + time + ", "
                    + "distance: " + String.format( "%.5f",lastWalker.travelledMax) + " ("
                    + String.format("%2.3f", recordRatio * 100) + "%)");
            */
            time = 0;
            activeWalkerIndex++;
            if (activeWalkerIndex >= walkers.size()) {
                
                distanceAverageGeneration = distanceTotalGeneration / walkers.size();
                if (distanceAverageGeneration > distanceAverageGenerationRecord) {
                    distanceAverageGenerationRecord = distanceAverageGeneration;
                }
                
                plot.addValue(0, distanceRecord);
                plot.addValue(1, distanceRecordGeneration);
                plot.addValue(2, distanceAverageGenerationRecord);
                plot.addValue(3, distanceAverageGeneration);
                
                
                //distanceAverageGenerationList.add(distanceAverageGeneration);
                //distanceAverageGenerationRecordList.add(distanceAverageGenerationRecord);
                //distanceRecordGenerationList.add(distanceRecordGeneration);
                //distanceRecordList.add(distanceRecord);
                
                String genOutput 
                        = "" + (generation+1) 
                        + "," + distanceAverageGeneration
                        + "," + distanceAverageGenerationRecord
                        + "," + distanceRecordGeneration
                        + "," + distanceRecord
                        + "";
                System.out.println(genOutput);
                /*
                System.out.println();
                System.out.println("Generation " + generation + " has ended!");
                System.out.println("Average distance this generation: " + String.format("%.5f", distanceAverageGeneration));
                System.out.println("Best average distance ever:       " + String.format("%.5f", distanceAverageGenerationRecord));
                System.out.println("Best distance this generation:    " + String.format("%.5f", distanceRecordGeneration));
                System.out.println("Best distance ever:               " + String.format("%.5f", distanceRecord));
                System.out.println();
                        */
                generation++;
                
                
                distanceTotalGeneration = 0.0;
                distanceRecordGeneration = 0.0;
                activeWalkerIndex = 0;
                if (config.RENEW_WORLD_EVERY_GENERATION) {
                    world = new World(config, random.nextLong());
                }
                walkers = GeneticAlgorithm.createPopulation(config, random, walkers);
                
            }
        }
    }
    
}
