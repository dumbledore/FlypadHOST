/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.flypad.io.connection;

/**
 *
 * @author albus
 */
public interface Connection extends DataListener {
    public void send(byte[] data);
    public void close();
}
