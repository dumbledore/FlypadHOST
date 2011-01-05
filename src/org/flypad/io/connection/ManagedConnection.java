/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.flypad.io.connection;

import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.UUID;
import org.flypad.util.log.Logger;

/**
 *
 * @author albus
 */
abstract class ManagedConnection
        implements Connection, TerminationListener {
    /**
     * Bluetooth Service name
     */
    protected static final String serviceName = "Flypad";

    /**
     * Bluetooth Service UUID of interest
     */
    protected static final String SERVICE_UUID =
            "8b58b6b2c6c749a6b154bf81033aa702";
//            "2d26618601fb47c28d9f10b8ec891363";

    protected UUID serviceUUID = new UUID(SERVICE_UUID, false);

    protected UUID[] uuids = {serviceUUID};

    /**
     * Local Bluetooth Manager
     */
    protected LocalDevice localDevice;

    /**
     * Disovery Agent
     */
    protected DiscoveryAgent discoveryAgent;

    protected final DataListener dataListener;
    protected TwoWayConnection connection;
    protected final Logger logger;

    public ManagedConnection(
            final DataListener dataListener,
            final Logger logger) {
        this.dataListener = dataListener;
        this.logger = logger;
    }

    public void send(byte[] data) {
        if (connection != null) {
            connection.send(data);
        }
    }

    public void receive(byte[] data) {
        dataListener.receive(data);
    }

    public void close() {
        if (connection != null) {
            connection.close();
        }
    }
}
