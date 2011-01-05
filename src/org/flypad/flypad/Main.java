/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.flypad.flypad;

import java.io.IOException;
import org.flypad.command.CommandExecutor;
import org.flypad.io.connection.Client;
import org.flypad.io.connection.Connection;
import org.flypad.io.connection.DataListener;
import org.flypad.util.log.ConsoleLogger;

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
            final ConsoleLogger logger = new ConsoleLogger();
            final CommandExecutor executor = new CommandExecutor();
            final Connection connection = new Client((DataListener) executor,logger);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
