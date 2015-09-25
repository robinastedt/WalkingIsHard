
package com.astedt.robin.walkingishard.util;

import java.util.Random;

/**
 *
 * @author robin
 */
public class Util {
    public static double getRandomRange(Random random, double a, double b) {
        return a + (b - a) * random.nextDouble();
    }
    public static double getRandomRange(Random random, int a, int b) {
        return a + random.nextInt(b - a);
    }
}
