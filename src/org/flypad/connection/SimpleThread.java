/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.flypad.connection;

/**
 *
 * @author albus
 */
public abstract class SimpleThread extends Thread {
    protected volatile boolean alive = true;

    public final void kill() {
        alive = false;
    }
}
