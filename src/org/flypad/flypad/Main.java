/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.flypad.flypad;

import java.io.IOException;
import org.flypad.connection.Client;
import org.flypad.connection.Connection;
import org.flypad.util.log.DataLogger;

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
            final DataLogger logger = new DataLogger();
//            final Connection connection = new Client(logger, logger);
            final Connection connection = new Client(logger, logger);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
