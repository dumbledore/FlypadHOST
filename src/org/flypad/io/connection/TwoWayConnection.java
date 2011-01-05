/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.flypad.io.connection;

import javax.microedition.io.StreamConnection;

/**
 *
 * @author albus
 */
class TwoWayConnection
        implements Connection, TerminationListener {
    private final ManagedConnection managedConnection;
    private final Reception reception;
    private final Transmission transmission;
    private final DataListener dataListener;

    public TwoWayConnection(
            final ManagedConnection managedConnection,
            final StreamConnection connection,
            final DataListener dataListener) {

        this.managedConnection = managedConnection;
        this.dataListener = dataListener;

        reception = new Reception(this, connection, this);
        reception.start();

        transmission = new Transmission(this, connection);
        transmission.start();
    }

    public final void send(byte[] data) {
        transmission.send(data);
    }

    public final void receive(byte[] data) {
        dataListener.receive(data);
    }

    public final void terminated() {
        reception.kill();
        transmission.kill();
        managedConnection.terminated();
    }

    public final void close() {
        reception.kill();
        transmission.kill();
    }
}
