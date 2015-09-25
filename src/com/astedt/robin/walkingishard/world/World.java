/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.astedt.robin.walkingishard.world;

import com.astedt.robin.walkingishard.Config;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author robin
 */
public class World {
    
    private long seed;
    private List<Double> points;
    private List<Double>[] octavePoints;
    private Random[] rngs;
    private int octaves;
    private double frequency;
    private double wavelength;
    
    public World(long seed, int octaves, double frequency) {
        if (octaves <= 0) throw new IllegalArgumentException("Octaves must be greater than 0! Octaves: " + octaves);
        if (frequency <= 0) throw new IllegalArgumentException("Frequency must be greater than 0! Frequency: " + frequency);
        
        this.seed = seed;
        this.octaves = octaves;
        this.frequency = frequency;
        wavelength = 1 / frequency;
        rngs = new Random[octaves];
        octavePoints = new List[octaves];
        Random seedRng = new Random(seed);
        for (int i = 0; i < octaves; i++) {
            rngs[i] = new Random(seedRng.nextLong());
            octavePoints[i] = new ArrayList<>();
        }
        points = new ArrayList<>();
    }
    
    public double getPoint(double x) {
        double xScaled = x * frequency;
        int x1 = (int)xScaled;
        int x2 = x1 + 1;
        double weight = (xScaled - x1);
        return Math.atan((x - Config.WALKER_SPAWN_X)) / Math.PI * 2  * ((1.0 - weight) * getIntPoint(x1) + weight * getIntPoint(x2));
    }
    
    private double getIntPoint(int i) {
        for (int oct = 0; oct < octaves; oct++) {
            while ((octavePoints[oct].size()-1) << oct < i+1) {
                octavePoints[oct].add(rngs[oct].nextDouble());
            }
        }
        
        while (points.size() - 1 < i) {
            int nextIndex = points.size();
            double newPoint = 0.0;
            int weight = 1;
            int weightTotal = 0;
            for (int oct = 0; oct < octaves; oct++) {
                weightTotal += weight;
                int octavePointIndex1 = nextIndex / weight;
                int octavePointIndex2 = octavePointIndex1 + 1;
                double interpolateWeight = (double)(nextIndex - (octavePointIndex1 * weight)) / weight;
                newPoint += weight * ((1.0 - interpolateWeight) * octavePoints[oct].get(octavePointIndex1) 
                         + interpolateWeight * octavePoints[oct].get(octavePointIndex2));
                weight *= 2;
            }
            newPoint /= weightTotal;
            points.add(newPoint);
        }
        
        return points.get(i);
    }
    
    public double getFrequency() {
        return frequency;
    }
    
    public double getWavelength() {
        return wavelength;
    }
}
