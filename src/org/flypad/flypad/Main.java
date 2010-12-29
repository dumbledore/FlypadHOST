/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.flypad.flypad;

import java.io.IOException;
import org.flypad.connection.Server;

/**
 *
 * @author albus
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try {
            final Server server = new Server();
            server.connect();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
