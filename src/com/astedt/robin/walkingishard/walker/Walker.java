/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.astedt.robin.walkingishard.walker;

import com.astedt.robin.walkingishard.Config;
import com.astedt.robin.walkingishard.genetics.Gene;
import com.astedt.robin.walkingishard.genetics.Genome;
import com.astedt.robin.walkingishard.util.BooleanMatrix;
import com.astedt.robin.walkingishard.world.World;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author robin
 */
public class Walker implements Comparable, Serializable {
    
    private Config config;
    
    public final Genome genome;
    public final List<Joint> joints;
    public final List<Muscle> muscles;
    
    public final List<Joint> disconnectedJoints;
    public final List<Muscle> disconnectedMuscles;
    
    public double travelledMax;
    public double lastDistanceRecordTime;
    
    public boolean alive;
    
    public Walker(Config config, Random random) {
        this(config, new Genome(config, random));
    }
    
    public Walker(Config config, Genome genome) {
        this.config = config;
        alive = true;
        travelledMax = 0.0;
        this.genome = genome;
        joints = new ArrayList<>();
        muscles = new ArrayList<>();
        int genomeSize = genome.getSize();
        boolean[][] connectionMatrix = new boolean[genomeSize][genomeSize];
        for (int id = 0; id < genomeSize; id++) {
            Gene gene = genome.getGene(id);
            
            Joint joint = new Joint(id, gene.x * config.WALKER_SIZE_RATIO + config.WALKER_SPAWN_X,
                    gene.y * config.WALKER_SIZE_RATIO + config.WALKER_SPAWN_Y, gene.r);
            
            
            joints.add(joint);

            if (id > 0) {
                for (int m = 0; m < id; m++) {
                    if (gene.connections[m]) {
                        Muscle muscle = new Muscle(joint, joints.get(m), gene.frequencies[m], gene.phases[m], gene.strengths[m]);
                        muscles.add(muscle);
                        connectionMatrix[m][id] = true;
                        connectionMatrix[id][m] = true;
                    }
                }
            }
        }
        
        boolean[][] A = connectionMatrix;
        boolean[][] B = A;
        
        for (int i = 0; i < genomeSize-1; i++) {
            A = BooleanMatrix.multiply(A, connectionMatrix);
            B = BooleanMatrix.add(A, B);
        }
        
        disconnectedJoints = new ArrayList<>();
        disconnectedMuscles = new ArrayList<>();
        
        for (int i = 1; i < genomeSize; i++) {
            if (B[0][i] == false) {
                Joint disconnectedJoint = joints.get(i);
                disconnectedJoints.add(disconnectedJoint);
                for (Muscle muscle : muscles) {
                    if (muscle.joint1 == disconnectedJoint) {
                        disconnectedMuscles.add(muscle);
                    }
                }
            }
        }
        
        
        
        for (Joint joint : disconnectedJoints) {
            joints.remove(joint);
        }
        
        for (Muscle muscle : disconnectedMuscles) {
            muscles.remove(muscle);
        }
        
        double xTotal = 0.0;
        for (Joint joint : joints) {
            xTotal += joint.x;
        }
        xTotal /= joints.size();
        
        for (Joint joint : joints) {
            joint.x += (config.WALKER_SPAWN_X - xTotal);
        }
        
    }
    
    public void step(long time, World world) {
        // Change momentum
        for (Muscle muscle : muscles) {
            Joint joint1 = muscle.joint1;
            Joint joint2 = muscle.joint2;
            double dx = joint2.x - joint1.x;
            double dy = joint2.y - joint1.y;
            double lengthSqr = dx * dx + dy * dy;
            double length = Math.sqrt(lengthSqr);
            double lengthPref = muscle.baseLength * (0.75 + 0.25 * Math.sin(muscle.phase + muscle.frequency * time));
            double lengthDiff = length - lengthPref;
            
            double dxv = joint1.xv - joint2.xv;
            double dyv = joint1.yv - joint2.yv;
            double dv = Math.sqrt(dxv * dxv + dyv * dyv);
            
            //Apply force towards or from each other depending on muscle strain
            joint1.xv += dx / length * lengthDiff * muscle.strength * config.WALKER_MUSCLE_FORCE_MULTIPLIER;
            joint1.yv += dy / length * lengthDiff * muscle.strength * config.WALKER_MUSCLE_FORCE_MULTIPLIER;
            joint2.xv -= dx / length * lengthDiff * muscle.strength * config.WALKER_MUSCLE_FORCE_MULTIPLIER;
            joint2.yv -= dy / length * lengthDiff * muscle.strength * config.WALKER_MUSCLE_FORCE_MULTIPLIER;
            
            
            //Dampen relative speed in the direction towards each other
            joint1.xv -= Math.abs(dx) / length * dxv * config.MUSCLE_DAMPENING;
            joint1.yv -= Math.abs(dy) / length * dyv * config.MUSCLE_DAMPENING;
            joint2.xv += Math.abs(dx) / length * dxv * config.MUSCLE_DAMPENING;
            joint2.yv += Math.abs(dy) / length * dyv * config.MUSCLE_DAMPENING;
            
        }
        
        for (Joint joint : joints) {
            joint.yv += config.GRAVITY;
        }
        
        // Change position
        for (Joint joint : joints) {
            
            //Apply momentum
            joint.x += joint.xv;
            joint.y += joint.yv;
        }
        
        // Collision detection
        for (Joint joint : joints) {
            //Collision joints
            for (Joint joint2 : joints) {
                if (joint2 != joint) {
                    double dx = joint2.x - joint.x;
                    double dy = joint2.y - joint.y;
                    double dSqr = dx * dx + dy * dy;
                    double dMin = joint.r + joint2.r;
                    double dMinSqr = dMin * dMin;
                    if (dSqr < dMinSqr) {
                        double d = Math.sqrt(dSqr);
                        double f = dMin - d;
                        
                        double r = joint.r + joint2.r;
                        
                        double dxv = joint.xv - joint2.xv;
                        double dyv = joint.yv - joint2.yv;
                        
                        double dv = Math.sqrt(dxv * dxv + dyv * dyv);
                        
                        //Fix position, can not be inside eachother
                        joint.x -= dx / d * f / 2;
                        joint.y -= dy / d * f / 2;
                        joint2.x += dx / d * f / 2;
                        joint2.y += dy / d * f / 2;
                        
                        //Dampen relative speed in the direction towards each other
                        joint.xv -= dx / d * dv * config.COLLISION_ENERGY_CONSERVATION;
                        joint.yv -= dy / d * dv * config.COLLISION_ENERGY_CONSERVATION;
                        joint2.xv += dx / d * dv * config.COLLISION_ENERGY_CONSERVATION;
                        joint2.yv += dy / d * dv * config.COLLISION_ENERGY_CONSERVATION;
                        
                        //Apply friction perpendicular to each other
                        joint.xv -= Math.abs(dy) / d * dxv * config.FRICTION;
                        joint.yv -= Math.abs(dx) / d * dyv * config.FRICTION;
                        joint2.xv -= Math.abs(dy) / d * -dxv * config.FRICTION;
                        joint2.yv -= Math.abs(dx) / d * -dyv * config.FRICTION;
                        
                    }
                }
            }
            
            // Collision left boundary
            if (joint.x-joint.r < 0.0) {
                joint.x = joint.r;
                joint.xv = -joint.xv * config.COLLISION_ENERGY_CONSERVATION;
                joint.yv -= joint.yv * config.FRICTION;
            }
            // Collision floor
            if (joint.y + joint.r > world.getPoint(joint.x)) {
                joint.y = world.getPoint(joint.x) - joint.r;
                joint.yv = -joint.yv * config.COLLISION_ENERGY_CONSERVATION;
                joint.xv -= joint.xv * config.FRICTION;
                if (config.KILL_ON_HEAD_COLLISION && joint.id == 0) {
                    alive = false;
                }
            }
            
            if (Math.abs(joint.xv) > 1.0) {
                System.out.println(joint.id + ": " + joint.xv);
            }
        }
        // Measure distance
        double xTotal = 0.0;
        for (Joint joint : joints) {
            xTotal += joint.x;
        }
        xTotal /= joints.size();
        xTotal -= config.WALKER_SPAWN_X;
        if (xTotal > travelledMax) {
            travelledMax = xTotal;
            lastDistanceRecordTime = time;
        }
    }

    @Override
    public int compareTo(Object t) {
        Walker wt = (Walker)t;
        if (travelledMax < wt.travelledMax) return 1;
        else if (travelledMax == wt.travelledMax) return 0;
        else return -1;
    }
}
