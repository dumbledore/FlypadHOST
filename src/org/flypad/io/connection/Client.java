/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.flypad.io.connection;

import org.flypad.util.SimpleThread;
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

    private ServerDiscoverer serverDiscoverer;
    
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

    public void terminated() {
        connect();
    }

    private synchronized void connect() {
        if (
                serverDiscoverer != null
                && serverDiscoverer.isWorking()) {
            logger.log("already searching");
            return;
        }

        serverDiscoverer = new ServerDiscoverer(this);
        serverDiscoverer.start();
    }

    class ServerDiscoverer extends SimpleThread {
        private final Client client;

        public ServerDiscoverer(final Client client) {
            this.client = client;
        }
        
        public void run() {
            String url = null;
            
            try {
                while (url == null) {
                    logger.log("Searching for host...");
                    url = discoveryAgent.selectService(
                            serviceUUID,
                            ServiceRecord.NOAUTHENTICATE_NOENCRYPT,
                            false);

                    if (url == null) {
                        logger.log("Couldn't find host");
                    }
                }

                logger.log("Host found. Connecting...");
                logger.log(url);

                StreamConnection sc = (StreamConnection) Connector.open(url);
                logger.log("Connected!");

                connection = new TwoWayConnection(client, sc, client);
            } catch (IOException e) {
                logger.log(e.getMessage());
            }
        }
    }
}
