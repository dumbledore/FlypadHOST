/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.flypad.command;

/**
 *
 * @author albus
 */
public interface Commands {
    public static final byte TOUCHPAD               = 0;
    public static final byte KEYBOARD               = 1;
    public static final byte DRIVING_WHEEL_XYZ_DATA = 2;
    public static final byte DRIVING_WHEEL_RESET    = 3;
    public static final byte DRIVING_WHEEL_ANALOG   = 4;
    public static final byte DRIVING_WHEEL_DIGITAL  = 5;
}
