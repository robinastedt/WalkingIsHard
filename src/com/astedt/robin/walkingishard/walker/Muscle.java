/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.astedt.robin.walkingishard.walker;

/**
 *
 * @author robin
 */
public class Muscle {
    public final Joint joint1, joint2;
    public final double frequency;
    public final double phase;
    public final double strength;
    public final double baseLength;
    
    public Muscle(Joint joint1, Joint joint2, double frequency, double phase, double strength) {
        this.joint1 = joint1;
        this.joint2 = joint2;
        this.frequency = frequency;
        this.phase = phase;
        this.strength = strength;
        
        double dx = joint1.x - joint2.x;
        double dy = joint1.y - joint2.y;
        double tempTaseLength = Math.sqrt(dx * dx + dy * dy);
        if (tempTaseLength < (joint1.r + joint2.r) * 2) baseLength = (joint1.r + joint2.r) * 2;
        else baseLength = tempTaseLength;
    }
}
