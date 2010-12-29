/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.flypad.connection;

import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.UUID;

/**
 *
 * @author albus
 */
public abstract class Base {
    /**
     * Bluetooth Service name
     */
    protected static final String serviceName = "Flypad";

    /**
     * Bluetooth Service UUID of interest
     */
    protected static final String SERVICE_UUID =
            "2d26618601fb47c28d9f10b8ec891363";

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
}
