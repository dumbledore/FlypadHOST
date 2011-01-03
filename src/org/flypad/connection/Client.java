/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.flypad.connection;

import java.io.IOException;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.ServiceRecord;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import org.flypad.util.log.Logger;

/**
 *
 * @author albus
 */
public class Client extends ManagedConnection {
    
    public Client(
            final DataListener dataListener,
            final Logger logger)
            throws IOException {

        super(dataListener, logger);
        /*
         * Retrieve the local device to get to the Bluetooth Manager
         */
        localDevice = LocalDevice.getLocalDevice();

        /*
         * Clients retrieve the discovery agent
         */
        discoveryAgent = localDevice.getDiscoveryAgent();

        logger.log("Client created.");

        connect();
    }

    private void connect() throws IOException {
        String url = null;

        while (url == null) {
            logger.log("Searching for host...");
            url = discoveryAgent.selectService(
                    serviceUUID,
                    ServiceRecord.NOAUTHENTICATE_NOENCRYPT,
                    false);

            if (url == null) {
                throw new IOException("Couldn't find host");
            }
        }

        logger.log("Host found. Connecting...");
        logger.log(url);

        StreamConnection sc = (StreamConnection) Connector.open(url);
        logger.log("Connected!");
        
        connection = new PhysicalConnection(this, sc, dataListener);
    }

    public void terminated() {
        try {
            connect();
        } catch (IOException e) {
            throw new RuntimeException("Couldn't reconnect!");
        }
    }
}
