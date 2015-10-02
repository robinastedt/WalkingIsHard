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
    
    private Config config;
    private long seed;
    private List<Double> points;
    private List<Double>[] octavePoints;
    private Random[] rngs;
    private int octaves;
    private double frequency;
    private double wavelength;
    
    public World(Config config, long seed) {
        
        this.config = config;
        this.seed = seed;
        this.octaves = config.WORLD_OCTAVES;
        this.frequency = config.WORLD_FREQUENCY;
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
        double xScaled = x * getFrequency(x);
        int x1 = (int)xScaled;
        int x2 = x1 + 1;
        double weight = (xScaled - x1);
        return Math.atan((x - config.WALKER_SPAWN_X)) / Math.PI * 2  * ((1.0 - weight) * getIntPoint(x1) + weight * getIntPoint(x2));
        //return ((1.0 - weight) * getIntPoint(x1) + weight * getIntPoint(x2));
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
    
    public double getFrequency(double x) {
        return frequency * (x / config.WORLD_FREQUENCY_DISTANCE_RATIO + 1.0);
    }
    
    public double getWavelength(double x) {
        return wavelength / (x / config.WORLD_FREQUENCY_DISTANCE_RATIO + 1.0);
    }
}
