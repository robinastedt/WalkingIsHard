/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.astedt.robin.walkingishard;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 *
 * @author robin
 */
public class KeyHandler extends KeyAdapter {

    private Main main;
    private Config config;
    
    public KeyHandler(Main main, Config config) {
        this.main = main;
        this.config = config;
    }
    
    @Override
    public void keyPressed(KeyEvent event) {
        int keyCode = event.getKeyCode();
        if (keyCode == KeyEvent.VK_SPACE) {
            main.timeSlow ^= true;
        }
        else if (keyCode == KeyEvent.VK_R) {
            main.render ^= true;
        }
        else if (keyCode == KeyEvent.VK_LEFT) {
            if (main.dc.xScrollOffset > -config.WALKER_SPAWN_X + 1.1) {
                main.dc.xScrollOffset -= 0.1;
            }
        }
        else if (keyCode == KeyEvent.VK_RIGHT) {
            main.dc.xScrollOffset += 0.1;
        }
    }
            
}
