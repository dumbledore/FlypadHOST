/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.flypad.command;

import org.flypad.io.connection.DataListener;

/**
 *
 * @author albus
 */
public class CommandExecutor
        implements DataListener, Commands {

    public void receive(byte[] data) {
        
        try {
            final byte command = data[0];
            
            switch (command) {
                case TOUCHPAD:
                    System.out.println("TOUCHPAD!");
                    break;

                case KEYBOARD:
                    System.out.println("KEYBOARD");
                    break;

                case DRIVING_WHEEL:
                    final double x, y, z;
                    x = readDouble(data, 1);
                    y = readDouble(data, 10);
                    z = readDouble(data, 19);
                    
                    System.out.println("X: " + x);
                    System.out.println("Y: " + y);
                    System.out.println("Z: " + z);
                    break;
                    
                default:
                    System.out.println("UNKNOWN");
            }
        } catch (Exception e) {
            System.out.println("WRONG DATA");
        }
    }

    private static double readDouble(
            final byte[] data, final int offset) {
        return Double.longBitsToDouble(readLong(data, offset));
    }

    private static long readLong(
            final byte[] data, final int offset) {
        return
                ((long)(readInt(data, offset)) << 32) +
                (readInt(data, offset + 4) & 0xFFFFFFFFL);
    }

    private static int readInt(
            final byte[] data, final int offset) {
        return (
                (data[offset]   << 24) +
                (data[offset + 1] << 16) +
                (data[offset + 2] << 8 ) +
                (data[offset + 3]      )
                );
    }
}
