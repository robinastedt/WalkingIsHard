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
    
    public KeyHandler(Main main) {
        this.main = main;
    }
    
    @Override
    public void keyPressed(KeyEvent event) {
        int keyCode = event.getKeyCode();
        if (keyCode == KeyEvent.VK_SPACE) {
            main.timeSlow ^= true;
        }
    }
            
}
