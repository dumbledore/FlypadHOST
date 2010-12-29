/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.flypad.connection;

import java.io.IOException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnectionNotifier;

/**
 *
 * @author albus
 */
public class Server extends Base {
    /**
     * Define the server connection URL
     */
    private final String connURL =
            "btspp://localhost:" + serviceUUID.toString() + ";"
            + "name=" + serviceName;

    private final StreamConnectionNotifier server;
    private Reception reception;

    public Server() throws IOException {

        /*
         * Retrieve the local device to get the Bluetooth Manager
         */
        localDevice = LocalDevice.getLocalDevice();

        /*
         * Servers set the discoverable mode to GIAC
         */
        localDevice.setDiscoverable(DiscoveryAgent.GIAC);

        /*
         * Create a server connection (a notifier)
         */
        server = (StreamConnectionNotifier) Connector.open(connURL);
        System.out.println("Server is running...");
    }

    public final void connect() throws IOException {
        if (reception == null) {
            reception = new Reception(server);
            reception.start();
        }
    }
}
