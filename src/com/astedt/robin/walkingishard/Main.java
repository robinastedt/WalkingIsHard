
package com.astedt.robin.walkingishard;

import com.astedt.robin.walkingishard.genetics.GeneticAlgorithm;
import com.astedt.robin.walkingishard.render.DrawingComponent;
import com.astedt.robin.walkingishard.plot.Plot;
import com.astedt.robin.walkingishard.walker.Walker;
import com.astedt.robin.walkingishard.world.World;
import java.awt.Color;
import java.awt.Dimension;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
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
public class Main implements Serializable {
    
    
    public boolean running;
    private Random random;
    public List<Walker> walkers;
    public long generation;
    public int activeWalkerIndex;
    public World world;
    public transient JFrame window;
    public transient DrawingComponent dc;
    private Plot plot;
    private Config config;
    private long time;
    
    public transient boolean timeSlow;
    public transient boolean render;
    
    public double distanceRecord;
    public double distanceRecordGeneration;
    private double distanceTotalGeneration;
    public double distanceAverageGeneration;
    public double distanceAverageGenerationRecord;
    
    //Session paramters
    private static boolean saving;
    private static String saveFilename;
    private static boolean loading;
    private static String loadFilename;
    private static boolean noPrint;
    public static boolean noGraphics;
    
    private static final String usage = "Usage: help | nographics | noprint | save [filename] | load [filename] | loadsave [filename]\n"
            + "Tip: When using nographics, run with java -jar WalkingIsHard.jar. "
            + "Otherwise you won't get printouts in the console and you'll have to manually terminate the application.";
    
    public static void main(String[] args) {
        
        saving = false;
        loading = false;
        noPrint = false;
        noGraphics = false;
        
        if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
            System.out.println(usage);
            System.exit(0);
        }
        
        for (int i = 0; i < args.length; i++) {
            if (args[i].equalsIgnoreCase("save")) {
                i++;
                if (args.length <= i) {
                    System.out.println("Usage: save [filename]");
                    System.exit(1);
                }
                saving = true;
                saveFilename = args[i];
            }
            else if (args[i].equalsIgnoreCase("load")) {
                i++;
                if (args.length <= i) {
                    System.out.println("Usage: load [filename]");
                    System.exit(1);
                }
                loading = true;
                loadFilename = args[i];
            }
            else if (args[i].equalsIgnoreCase("loadsave")) {
                i++;
                if (args.length <= i) {
                    System.out.println("Usage: loadsave [filename]");
                    System.exit(1);
                }
                loading = true;
                saving = true;
                loadFilename = args[i];
                saveFilename = args[i];
            }
            else if (args[i].equalsIgnoreCase("noprint")) {
                noPrint = true;
            }
            else if (args[i].equalsIgnoreCase("nographics")) {
                noGraphics = true;
            }
            else {
                System.out.println("Unkown argument: " + args[i] + "\n" + usage);
                System.exit(1);
            }
        }
        
        Main main;
        
        if (loading) {
            main = load(loadFilename);
        }
        else {
            Config config = new Config("config.properties");
            config.load();
            config.save();
            main = new Main(config);
        }
        
        if (!noGraphics) {
            main.createWindow();
            main.timeSlow = true;
        }
        else {
            main.timeSlow = false;
        }
        main.run();
    }
    
    public Main(Config config) {
        this.config = config;
        init();
    }
    
    private void createWindow() {
        plot.createWindow();
        window = new JFrame();
        window.setTitle("Walking is hard");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dc = new DrawingComponent(this, config);
        dc.setPreferredSize(new Dimension(config.WIDTH, config.HEIGHT));
        window.add(dc);
        window.addKeyListener(new KeyHandler(this, config));
        window.setResizable(true);
        window.pack();
        window.pack();
        window.setVisible(true);
        render = true;
    }
    
    private void run() {
        
        if (saving) save(saveFilename);
        
        while (running) {
            synchronized (this) {
                step();
            }
            if (render) window.repaint();
            
            if (timeSlow) {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            
        }
        
        window.dispose();
    }
    
    private void init() {
        
        random = new Random();
        time = 0;
        timeSlow = true;
        render = false;
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

                
                if (!noPrint) {
                    String genOutput 
                        = "" + (generation+1) 
                        + "," + distanceAverageGeneration
                        + "," + distanceAverageGenerationRecord
                        + "," + distanceRecordGeneration
                        + "," + distanceRecord
                        + "";
                    System.out.println(genOutput);
                }
                
                generation++;
                
                
                distanceTotalGeneration = 0.0;
                distanceRecordGeneration = 0.0;
                activeWalkerIndex = 0;
                if (config.RENEW_WORLD_EVERY_GENERATION) {
                    world = new World(config, random.nextLong());
                }
                walkers = GeneticAlgorithm.createPopulation(config, random, walkers);
                if (saving) {
                    save(saveFilename);
                }
            }
        }
    }
    
    public void save(String filename) {
        try {
            FileOutputStream fileOut = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.close();
                fileOut.close();
        } 
        catch (IOException i) {
            i.printStackTrace();
        }
        if (!noPrint) System.out.println("Saved to " + filename);
    }
    
    public static Main load(String filename) {
        Main main = null;
        try {
            FileInputStream fileIn = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            main = (Main) in.readObject();
            main.timeSlow = true;
            in.close();
            fileIn.close();
        }
        catch (IOException i) {
            System.out.println("Could not read file: " + filename);
            i.printStackTrace();
            return null;
        }
        catch (ClassNotFoundException c) {
            System.out.println("Main class not found");
            c.printStackTrace();
            return null;
        }
        if (!noPrint) System.out.println("Loaded from " + filename);
        return main;
    }
}
