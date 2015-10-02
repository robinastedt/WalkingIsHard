
package com.astedt.robin.walkingishard.walker;

import java.io.Serializable;

/**
 *
 * @author robin
 * 
 */

public class Joint implements Serializable {
    public final int id;
    public double x, y, r, xv, yv;
    
    public Joint(int id, double x, double y, double r) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.r = r;
        xv = 0.0;
        yv = 0.0;
        
    }
}
