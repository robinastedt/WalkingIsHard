
package com.astedt.robin.walkingishard;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import nu.studer.java.util.OrderedProperties;

/**
 *
 * @author robin
 */
public class Config {
    public int WIDTH = 800;
    public int HEIGHT = 600;
    
    public int WORLD_OCTAVES = 8;
    public double WORLD_FREQUENCY = 10;
    public double WORLD_FREQUENCY_DISTANCE_RATIO = 10.0;
    
    public int EVAL_TIME_OUT = 50 * 5;
    public int POPULATION_SIZE = 100;
    public double FITNESS_BASE = 1.0;
    public double FITNESS_SPEED_FACTOR = 100.0;
    public boolean RENEW_WORLD_EVERY_GENERATION = false;
    public boolean KILL_ON_HEAD_COLLISION = true;
    
    public double MUTATION_CHANGE_CONNECTION = 0.02;
    public double MUTATION_CHANGE_FREQUENCY = 0.02;
    public double MUTATION_CHANGE_PHASE = 0.02;
    public double MUTATION_CHANGE_STRENGTH = 0.02;
    public double MUTATION_CHANGE_POSITION = 0.02;
    public double MUTATION_REMOVE_GENE = 0.03;
    public double MUTATION_ADD_GENE = 0.03;
    
    public double GRAVITY = 0.001;
    public double COLLISION_ENERGY_CONSERVATION = 0.25;
    public double FRICTION = 0.9;
    public double MUSCLE_DAMPENING = 0.1;
  
    public double WALKER_MUSCLE_FORCE_MULTIPLIER = 0.25;
    public double WALKER_MUSCLE_FREQUENCY_MAX = Math.PI / 25;
    public double WALKER_SIZE_RATIO = 0.25;
    public double WALKER_SPAWN_X = 10.0;
    public double WALKER_SPAWN_Y = -1.0;
    
    public int RANDOM_GENOME_ADD_JOINT_RATIO = 3;
    public int RANDOM_GENOME_NOT_ADD_MUSCLE_RATIO = 3;
    public double RANDOM_GENE_RADIUS_MIN = 0.05;
    public double RANDOM_GENE_RADIUS_MAX = 0.2;
    
    public String filename;
    
    public Config(String filename) {
        this.filename = filename;
    }
    
    
    public void save() {
        OrderedProperties prop = new OrderedProperties();
	OutputStream output = null;

	try {
            output = new FileOutputStream(filename);
            
            // set the properties value
            prop.setProperty("WIDTH", Integer.toString(WIDTH));
            prop.setProperty("HEIGHT", Integer.toString(HEIGHT));
            
            prop.setProperty("WORLD_OCTAVES", Integer.toString(WORLD_OCTAVES));
            prop.setProperty("WORLD_FREQUENCY", Double.toString(WORLD_FREQUENCY));
            prop.setProperty("WORLD_FREQUENCY_DISTANCE_RATIO", Double.toString(WORLD_FREQUENCY_DISTANCE_RATIO));
            
            prop.setProperty("EVAL_TIME_OUT", Integer.toString(EVAL_TIME_OUT));
            prop.setProperty("POPULATION_SIZE", Integer.toString(POPULATION_SIZE));
            prop.setProperty("FITNESS_BASE", Double.toString(FITNESS_BASE));
            prop.setProperty("FITNESS_SPEED_FACTOR", Double.toString(FITNESS_SPEED_FACTOR));
            prop.setProperty("RENEW_WORLD_EVERY_GENERATION", Boolean.toString(RENEW_WORLD_EVERY_GENERATION));
            prop.setProperty("KILL_ON_HEAD_COLLISION", Boolean.toString(KILL_ON_HEAD_COLLISION));
            
            prop.setProperty("MUTATION_CHANGE_CONNECTION", Double.toString(MUTATION_CHANGE_CONNECTION));
            prop.setProperty("MUTATION_CHANGE_FREQUENCY", Double.toString(MUTATION_CHANGE_FREQUENCY));
            prop.setProperty("MUTATION_CHANGE_PHASE", Double.toString(MUTATION_CHANGE_PHASE));
            prop.setProperty("MUTATION_CHANGE_STRENGTH", Double.toString(MUTATION_CHANGE_STRENGTH));
            prop.setProperty("MUTATION_CHANGE_POSITION", Double.toString(MUTATION_CHANGE_POSITION));
            prop.setProperty("MUTATION_REMOVE_GENE", Double.toString(MUTATION_REMOVE_GENE));
            prop.setProperty("MUTATION_ADD_GENE", Double.toString(MUTATION_ADD_GENE));
            
            prop.setProperty("GRAVITY", Double.toString(GRAVITY));
            prop.setProperty("COLLISION_ENERGY_CONSERVATION", Double.toString(COLLISION_ENERGY_CONSERVATION));
            prop.setProperty("FRICTION", Double.toString(FRICTION));
            prop.setProperty("MUSCLE_DAMPENING", Double.toString(MUSCLE_DAMPENING));
            
            prop.setProperty("WALKER_MUSCLE_FORCE_MULTIPLIER", Double.toString(WALKER_MUSCLE_FORCE_MULTIPLIER));
            prop.setProperty("WALKER_MUSCLE_FREQUENCY_MAX", Double.toString(WALKER_MUSCLE_FREQUENCY_MAX));
            prop.setProperty("WALKER_SIZE_RATIO", Double.toString(WALKER_SIZE_RATIO));
            prop.setProperty("WALKER_SPAWN_X", Double.toString(WALKER_SPAWN_X));
            prop.setProperty("WALKER_SPAWN_Y", Double.toString(WALKER_SPAWN_Y));
            
            prop.setProperty("RANDOM_GENOME_ADD_JOINT_RATIO", Integer.toString(RANDOM_GENOME_ADD_JOINT_RATIO));
            prop.setProperty("RANDOM_GENOME_NOT_ADD_MUSCLE_RATIO", Integer.toString(RANDOM_GENOME_NOT_ADD_MUSCLE_RATIO));
            prop.setProperty("RANDOM_GENE_RADIUS_MIN", Double.toString(RANDOM_GENE_RADIUS_MIN));
            prop.setProperty("RANDOM_GENE_RADIUS_MAX", Double.toString(RANDOM_GENE_RADIUS_MAX));
            
            // save properties to project root folder
            prop.store(output, null);
	} 
        catch (IOException ex) {
            JOptionPane.showMessageDialog(null, ex.toString(), "Error", JOptionPane.ERROR_MESSAGE);
	}
        finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
	}
    }
    
    public void load() {
        OrderedProperties prop = new OrderedProperties();
	InputStream input = null;

	try {

            input = new FileInputStream(filename);

            // load a properties file
            prop.load(input);

            // get the properties
            
            WIDTH = Integer.parseInt(prop.getProperty("WIDTH", Integer.toString(WIDTH)));
            HEIGHT = Integer.parseInt(prop.getProperty("HEIGHT", Integer.toString(HEIGHT)));
            
            WORLD_OCTAVES = Integer.parseInt(prop.getProperty("WORLD_OCTAVES", Integer.toString(WORLD_OCTAVES)));
            WORLD_FREQUENCY = Double.parseDouble(prop.getProperty("WORLD_FREQUENCY", Double.toString(WORLD_FREQUENCY)));
            WORLD_FREQUENCY_DISTANCE_RATIO = Double.parseDouble(prop.getProperty("WORLD_FREQUENCY_DISTANCE_RATIO", Double.toString(WORLD_FREQUENCY_DISTANCE_RATIO)));
            
            EVAL_TIME_OUT = Integer.parseInt(prop.getProperty("EVAL_TIME_OUT", Integer.toString(EVAL_TIME_OUT)));
            POPULATION_SIZE = Integer.parseInt(prop.getProperty("POPULATION_SIZE", Integer.toString(POPULATION_SIZE)));
            FITNESS_BASE = Double.parseDouble(prop.getProperty("FITNESS_BASE", Double.toString(FITNESS_BASE)));
            FITNESS_SPEED_FACTOR = Double.parseDouble(prop.getProperty("FITNESS_SPEED_FACTOR", Double.toString(FITNESS_SPEED_FACTOR)));
            RENEW_WORLD_EVERY_GENERATION = Boolean.parseBoolean(prop.getProperty("RENEW_WORLD_EVERY_GENERATION", Boolean.toString(RENEW_WORLD_EVERY_GENERATION)));
            KILL_ON_HEAD_COLLISION = Boolean.parseBoolean(prop.getProperty("KILL_ON_HEAD_COLLISION", Boolean.toString(KILL_ON_HEAD_COLLISION)));
            
            MUTATION_CHANGE_CONNECTION = Double.parseDouble(prop.getProperty("MUTATION_CHANGE_CONNECTION", Double.toString(MUTATION_CHANGE_CONNECTION)));
            MUTATION_CHANGE_FREQUENCY = Double.parseDouble(prop.getProperty("MUTATION_CHANGE_FREQUENCY", Double.toString(MUTATION_CHANGE_FREQUENCY)));
            MUTATION_CHANGE_PHASE = Double.parseDouble(prop.getProperty("MUTATION_CHANGE_PHASE", Double.toString(MUTATION_CHANGE_PHASE)));
            MUTATION_CHANGE_STRENGTH = Double.parseDouble(prop.getProperty("MUTATION_CHANGE_STRENGTH", Double.toString(MUTATION_CHANGE_STRENGTH)));
            MUTATION_CHANGE_POSITION = Double.parseDouble(prop.getProperty("MUTATION_CHANGE_POSITION", Double.toString(MUTATION_CHANGE_POSITION)));
            MUTATION_REMOVE_GENE = Double.parseDouble(prop.getProperty("MUTATION_REMOVE_GENE", Double.toString(MUTATION_REMOVE_GENE)));
            MUTATION_ADD_GENE = Double.parseDouble(prop.getProperty("MUTATION_ADD_GENE", Double.toString(MUTATION_ADD_GENE)));
            
            GRAVITY = Double.parseDouble(prop.getProperty("GRAVITY", Double.toString(GRAVITY)));
            COLLISION_ENERGY_CONSERVATION = Double.parseDouble(prop.getProperty("COLLISION_ENERGY_CONSERVATION", Double.toString(COLLISION_ENERGY_CONSERVATION)));
            FRICTION = Double.parseDouble(prop.getProperty("FRICTION", Double.toString(FRICTION)));
            MUSCLE_DAMPENING = Double.parseDouble(prop.getProperty("MUSCLE_DAMPENING", Double.toString(MUSCLE_DAMPENING)));
            
            WALKER_MUSCLE_FORCE_MULTIPLIER = Double.parseDouble(prop.getProperty("WALKER_MUSCLE_FORCE_MULTIPLIER", Double.toString(WALKER_MUSCLE_FORCE_MULTIPLIER)));
            WALKER_MUSCLE_FREQUENCY_MAX = Double.parseDouble(prop.getProperty("WALKER_MUSCLE_FREQUENCY_MAX", Double.toString(WALKER_MUSCLE_FREQUENCY_MAX)));
            WALKER_SIZE_RATIO = Double.parseDouble(prop.getProperty("WALKER_SIZE_RATIO", Double.toString(WALKER_SIZE_RATIO)));
            WALKER_SPAWN_X = Double.parseDouble(prop.getProperty("WALKER_SPAWN_X", Double.toString(WALKER_SPAWN_X)));
            WALKER_SPAWN_Y = Double.parseDouble(prop.getProperty("WALKER_SPAWN_Y", Double.toString(WALKER_SPAWN_Y)));
            
            RANDOM_GENOME_ADD_JOINT_RATIO = Integer.parseInt(prop.getProperty("RANDOM_GENOME_ADD_JOINT_RATIO", Integer.toString(RANDOM_GENOME_ADD_JOINT_RATIO)));
            RANDOM_GENOME_NOT_ADD_MUSCLE_RATIO = Integer.parseInt(prop.getProperty("RANDOM_GENOME_NOT_ADD_MUSCLE_RATIO", Integer.toString(RANDOM_GENOME_NOT_ADD_MUSCLE_RATIO)));
            RANDOM_GENE_RADIUS_MIN = Double.parseDouble(prop.getProperty("RANDOM_GENE_RADIUS_MIN", Double.toString(RANDOM_GENE_RADIUS_MIN)));
            RANDOM_GENE_RADIUS_MAX = Double.parseDouble(prop.getProperty("RANDOM_GENE_RADIUS_MAX", Double.toString(RANDOM_GENE_RADIUS_MAX)));

	} 
        catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Could not read file \"" + filename + "\"!\n"
                    + "The file will now be created with default values.", "Error", JOptionPane.ERROR_MESSAGE);
	} 
        finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
	}

    }
}