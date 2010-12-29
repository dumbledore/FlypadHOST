/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.flypad.connection;

import java.io.DataInputStream;
import java.io.IOException;
import javax.bluetooth.RemoteDevice;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

/**
 *
 * @author albus
 */
public class Reception
        extends Thread {
    
    private final StreamConnectionNotifier server;
    private StreamConnection client;
    private RemoteDevice remote;
    
    private volatile boolean alive = true;

    public Reception(final StreamConnectionNotifier server) {
        this.server = server;
    }

    public final void run() {
        try {
            /*
             * Await client connection
             */
            connect();

            /*
             * Start receiving data
             */
            int size;
            byte[] buffer;
            long t;
            DataInputStream receive = client.openDataInputStream();
            try {
                while (alive) {
                    size = receive.readShort();
                    t = System.currentTimeMillis();
                    buffer = new byte[size];
                    receive.readFully(buffer);
                    t = System.currentTimeMillis() - t;
                    System.out.println("[Recieved]: "
                            + new String(buffer)
                            + " (" + t + ")");
                }
            } finally {
                System.out.println("Closing connection...");
                receive.close();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void connect() throws IOException {
        /*
         * Accept a new client connection
         */
        System.out.println("Awaiting client connection...");
        client = server.acceptAndOpen();
        
        /*
         * Get a handle on the connection
         */
        remote = RemoteDevice.getRemoteDevice(client);

        System.out.println("New client connection to "
                + remote.getFriendlyName(false));
    }

    public final void kill() {
        alive = false;
    }
}
