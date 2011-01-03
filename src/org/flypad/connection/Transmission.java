/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.flypad.connection;

import java.io.DataOutputStream;
import java.io.IOException;
import javax.microedition.io.StreamConnection;
import org.flypad.util.DataQueue;

/**
 *
 * @author albus
 */
class Transmission extends SimpleThread {
    private final PhysicalConnection physicalConnection;
    private StreamConnection connection;
    private DataQueue queue = new DataQueue(128);

    public Transmission(
            final PhysicalConnection physicalConnection,
            final StreamConnection connection) {
        this.physicalConnection = physicalConnection;
        this.connection = connection;
    }

    public final void run() {
        try {
            DataOutputStream out = connection.openDataOutputStream();

            try {
                while(alive) {
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
            physicalConnection.terminated();
        }
    }

    public final void send(final byte[] data) {
        queue.enqueue(data);
    }
}
