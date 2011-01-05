/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.flypad.io.connection;

import java.io.DataOutputStream;
import java.io.IOException;
import javax.microedition.io.StreamConnection;
import org.flypad.util.DataQueue;

/**
 *
 * @author albus
 */
class Transmission extends OneWayConnection {
    private DataQueue queue = new DataQueue(128);

    public Transmission(
            final TwoWayConnection physicalConnection,
            final StreamConnection connection) {
        super(physicalConnection, connection);
    }

    public final void run() {
        try {
            DataOutputStream out = connection.openDataOutputStream();

            try {
                while(isWorking()) {
                    if (!queue.isEmpty()) {
                        byte[] data = queue.dequeue();
                        if (data != null) {
                            out.writeShort((short) data.length);
                            out.write(data);
                            out.flush();
                        }
                    } else {
                        try {
                            sleep(100);
                        } catch (InterruptedException e) {}
                    }
                }
            } finally {
                out.close();
            }
        } catch (IOException e) {
            root.terminated();
        }
    }

    public final void send(final byte[] data) {
        queue.enqueue(data);
    }
}
