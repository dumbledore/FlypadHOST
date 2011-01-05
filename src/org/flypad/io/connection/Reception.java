/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.flypad.io.connection;

import org.flypad.util.SimpleThread;
import java.io.DataInputStream;
import java.io.IOException;
import javax.microedition.io.StreamConnection;

/**
 *
 * @author albus
 */
class Reception extends OneWayConnection {

    private final DataListener dataListner;
    
    public Reception(
            final TwoWayConnection root,
            final StreamConnection connection,
            final DataListener dataListener
            ) {
        super(root, connection);
        this.dataListner = dataListener;
    }

    public final void run() {
        try {
            int size;
            byte[] buffer;
            DataInputStream in = connection.openDataInputStream();
            
            try {
                while (isWorking()) {
                    size = in.readShort();
                    buffer = new byte[size];
                    in.readFully(buffer);
                    dataListner.receive(buffer);
                }
            } finally {
                in.close();
            }
        } catch (IOException e){
            root.terminated();
        }
    }
}
