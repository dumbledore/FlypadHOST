/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.flypad.util.log;

import org.flypad.connection.DataListener;

/**
 *
 * @author albus
 */
public class DataLogger
        extends ConsoleLogger
        implements DataListener {

    public void receive(byte[] data) {
        log("{R} " + new String(data));
    }
}