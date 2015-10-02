
package com.astedt.robin.walkingishard.genetics;

import com.astedt.robin.walkingishard.Config;
import com.astedt.robin.walkingishard.Main;
import com.astedt.robin.walkingishard.util.Util;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author Robin
 */
public class Gene {
    
    private Config config;
    
    public int id;
    public double x, y, r;
    public boolean[] connections;
    public double[] frequencies;
    public double[] phases;
    public double[] strengths;
    
    public Gene(Config config, int id, double x, double y, double r, boolean[] connections, double[] frequencies, double[] phases, double[] strengths) {
        this.config = config;
        this.id = id;
        this.x = x;
        this.y = y;
        this.r = r;
        this.connections = connections;
        this.frequencies = frequencies;
        this.phases = phases;
        this.strengths = strengths;
    }
    
    public Gene(Config config, Random random, int id) {
        this.config = config;
        this.id = id;
        x = Util.getRandomRange(random, -1.0, 1.0);
        y = Util.getRandomRange(random, -1.0, 1.0);
        r = Util.getRandomRange(random, config.RANDOM_GENE_RADIUS_MIN * config.WALKER_SIZE_RATIO, config.RANDOM_GENE_RADIUS_MAX * config.WALKER_SIZE_RATIO);
        connections = new boolean[id];
        frequencies = new double[id];
        phases = new double[id];
        strengths = new double[id];
        for (int i = 0; i < id; i++) {
            if (random.nextInt(config.RANDOM_GENOME_NOT_ADD_MUSCLE_RATIO) == 0) {
                connections[i] = true;
                frequencies[i] = Util.getRandomRange(random, 0.0, config.WALKER_MUSCLE_FREQUENCY_MAX);
                phases[i] = Util.getRandomRange(random, 0.0, 2 * Math.PI);
                strengths[i] = Util.getRandomRange(random, 0.0, 1.0);
            }
        }
    }
    
    public Gene copy() {
        Gene gene = new Gene(config, id, x, y, r, 
                Arrays.copyOf(connections, id), 
                Arrays.copyOf(frequencies, id), 
                Arrays.copyOf(phases, id), 
                Arrays.copyOf(strengths, id));
        return gene;
    }
    
}
