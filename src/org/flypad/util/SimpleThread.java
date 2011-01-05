/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.flypad.util;

/**
 *
 * @author albus
 */
public abstract class SimpleThread extends Thread {
    private volatile boolean alive = true;

    public void kill() {
        alive = false;
    }

    public boolean isWorking() {
        return alive;
    }
}
