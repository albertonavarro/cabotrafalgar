/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.navid.trafalgar.input;

/**
 *
 * @author alberto
 */
public interface Command {
    
    String getName();
    
    void execute(float tpf);
    
}
