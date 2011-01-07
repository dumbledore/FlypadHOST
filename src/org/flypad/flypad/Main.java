/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.flypad.flypad;

import org.flypad.command.CommandExecutor;
import org.flypad.io.bluetooth.Client;
import org.flypad.io.bluetooth.Connection;

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
            final CommandExecutor executor = new CommandExecutor();
            final Connection connection = new Client(executor);
            executor.start();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
