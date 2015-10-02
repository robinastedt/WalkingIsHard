/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.astedt.robin.walkingishard.genetics;

import com.astedt.robin.walkingishard.Config;
import com.astedt.robin.walkingishard.util.Util;
import com.astedt.robin.walkingishard.walker.Walker;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Robin
 */
public class GeneticAlgorithm {
    public static List<Walker> createPopulation(Config config, Random random, List<Walker> oldWalkers) {
        List<Walker> newWalkers = new ArrayList<>();
        
        oldWalkers.sort(null);
        
        
        double totalFitness = 0.0;
        double[] fitnessThresholds = new double[oldWalkers.size()];
        int i = 0;
        for (Walker walker : oldWalkers){
            totalFitness += config.FITNESS_BASE
                    + walker.travelledMax
                    + config.FITNESS_SPEED_FACTOR * walker.travelledMax / (walker.lastDistanceRecordTime + 1);
            fitnessThresholds[i++] = totalFitness;
        }
        
        
        
        while (newWalkers.size() < oldWalkers.size()) {
            Walker walker1 = null, walker2 = null;
            double spin1 = random.nextDouble() * totalFitness;
            double spin2 = random.nextDouble() * totalFitness;
            int i1 = 0, i2 = 0;
            while (walker1 == null || walker2 == null) {
                if (walker1 == null) {
                    if (spin1 < fitnessThresholds[i1]) {
                        walker1 = oldWalkers.get(i1);
                    }
                    else {
                        i1++;
                    }
                }
                if (walker2 == null) {
                    if (spin2 < fitnessThresholds[i2]) {
                        walker2 = oldWalkers.get(i2);
                    }
                    else {
                        i2++;
                    }
                }
            }
            
            Genome genome1 = walker1.genome;
            Genome genome2 = walker2.genome;
            
            int splitPoint = random.nextInt(Math.min(genome1.getSize(), genome2.getSize()));
            Genome newGenome1 = new Genome(config, splitPoint, genome1, genome2);
            Genome newGenome2 = new Genome(config, splitPoint, genome2, genome1);
            
            newGenome1 = mutateGenome(config, random, newGenome1);
            newGenome2 = mutateGenome(config, random, newGenome2);
            
            newWalkers.add(new Walker(config, newGenome1));
            newWalkers.add(new Walker(config, newGenome2));
            
        }
        
        return newWalkers;
    }
    
    private static Genome mutateGenome(Config config, Random random, Genome g) {
        if (random.nextDouble() < config.MUTATION_REMOVE_GENE && g.getSize() > 1) {
            g.removeGene(g.getSize()-1);
        }
        
        if (random.nextDouble() < config.MUTATION_ADD_GENE) {
            Gene gene = new Gene(config, random, g.getSize());
            g.addGene(gene);
        }
        
        for (int i = 0; i < g.getSize(); i++) {
            Gene gene = g.getGene(i);
            
            if (random.nextDouble() < config.MUTATION_CHANGE_POSITION) {
                gene.x = Util.getRandomRange(random, -1.0, 1.0);
                gene.y = Util.getRandomRange(random, -1.0, 1.0);
            }
            
            for (int j = 0; j < gene.id; j++) {
                if (random.nextDouble() < config.MUTATION_CHANGE_CONNECTION) {
                    gene.connections[j] ^= true;
                }
                if (random.nextDouble() < config.MUTATION_CHANGE_FREQUENCY) {
                    gene.frequencies[j] = Util.getRandomRange(random, 0.0, config.WALKER_MUSCLE_FREQUENCY_MAX);
                }
                if (random.nextDouble() < config.MUTATION_CHANGE_PHASE) {
                    gene.phases[j] = Util.getRandomRange(random, 0.0, 2 * Math.PI);
                }
                if (random.nextDouble() < config.MUTATION_CHANGE_STRENGTH) {
                    gene.strengths[j] = Util.getRandomRange(random, 0.0, 1.0);
                }
            }
        }
        
        return g;
    }
}
