/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.flypad.connection;

import java.io.DataInputStream;
import java.io.IOException;
import javax.microedition.io.StreamConnection;

/**
 *
 * @author albus
 */
class Reception extends SimpleThread {

    private final PhysicalConnection root;
    private final StreamConnection connection;
    private final DataListener dataListner;
    
    public Reception(
            final PhysicalConnection root,
            final StreamConnection connection,
            final DataListener dataListener
            ) {
        this.root = root;
        this.connection = connection;
        this.dataListner = dataListener;
    }

    public final void run() {
        try {
            int size;
            byte[] buffer;
            DataInputStream in = connection.openDataInputStream();
            
            try {
                while (alive) {
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
