
package com.astedt.robin.walkingishard;

/**
 *
 * @author robin
 */
public class Config {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    
    public static final int WORLD_OCTAVES = 8;
    public static final double WORLD_FREQUENCY = 10;
    public static final double WORLD_FREQUENCY_DISTANCE_RATIO = 10.0;
    
    public static final long EVAL_TIME_OUT = 50 * 5;
    public static final int POPULATION_SIZE = 500;
    public static final double FITNESS_BASE = 1.0;
    public static final boolean RENEW_WORLD_EVERY_GENERATION = false;
    public static final boolean KILL_ON_HEAD_COLLISION = true;
    public static final double FITNESS_SPEED_FACTOR = 100.0;
    
    public static final double MUTATION_CHANGE_CONNECTION = 0.02;
    public static final double MUTATION_CHANGE_FREQUENCY = 0.02;
    public static final double MUTATION_CHANGE_PHASE = 0.02;
    public static final double MUTATION_CHANGE_STRENGTH = 0.02;
    public static final double MUTATION_CHANGE_POSITION = 0.02;
    public static final double MUTATION_REMOVE_GENE = 0.05;
    public static final double MUTATION_ADD_GENE = 0.05;
    
    public static final double GRAVITY = 0.001;
    public static final double COLLISION_ENERGY_CONSERVATION = 0.25;
    public static final double FRICTION = 0.9;
    public static final double MUSCLE_DAMPENING = 0.1;
  
    public static final double WALKER_MUSCLE_FORCE_MULTIPLIER = 0.25;
    public static final double WALKER_MUSCLE_FREQUENCY_MAX = Math.PI / 25;
    public static final double WALKER_SIZE_RATIO = 0.25;
    public static final double WALKER_SPAWN_X = 10.0;
    public static final double WALKER_SPAWN_Y = -1.0;
    
    public static final int RANDOM_GENOME_ADD_JOINT_RATIO = 2;
    public static final int RANDOM_GENOME_NOT_ADD_MUSCLE_RATIO = 2;
    public static final double RANDOM_GENE_RADIUS_MIN = 0.05;
    public static final double RANDOM_GENE_RADIUS_MAX = 0.2;
}