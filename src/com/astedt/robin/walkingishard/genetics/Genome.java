/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.astedt.robin.walkingishard.genetics;

import com.astedt.robin.walkingishard.Config;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Robin
 */
public class Genome {
    
    private final List<Gene> genes;
    
    public Genome(int splitPoint, Genome g1, Genome g2) {
        if (splitPoint >= Math.min(g1.getSize(), g2.getSize())) {
            throw new IllegalArgumentException("Split point does not fit inside both genomes!");
        }
        genes = new ArrayList<>();
        
        for (int i = 0; i  < g2.getSize(); i++) {
            Gene gene;
            if (i <= splitPoint) gene = g1.getGene(i).copy();
            else gene = g2.getGene(i).copy();
            genes.add(gene);
        }
    }
    
    public Genome(Random random) {
        genes = new ArrayList<>();
        
        genes.add(new Gene(random, 0));
        int geneId = 1;
        while (random.nextInt(Config.RANDOM_GENOME_ADD_JOINT_RATIO) > 0) {
            genes.add(new Gene(random, geneId));
            geneId++;
        }
    }
    
    
    
    public Gene getGene(int id) {
        return genes.get(id);
    }
    
    public void addGene(Gene gene) {
        genes.add(gene);
    }
    
    public void removeGene(int id) {
        genes.remove(id);
    }
    
    public int getSize() {
        return genes.size();
    }
}
