/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.flypad.io.connection;

import java.io.IOException;
import javax.microedition.io.StreamConnection;
import org.flypad.util.SimpleThread;

/**
 *
 * @author albus
 */
abstract class OneWayConnection extends SimpleThread {
    protected final TwoWayConnection root;
    protected final StreamConnection connection;

    public OneWayConnection(
            final TwoWayConnection root,
            final StreamConnection connection) {
        this.root = root;
        this.connection = connection;
    }

    public final void kill() {
        super.kill();
        try {
            connection.close();
        } catch (IOException e) {}
    }
}
