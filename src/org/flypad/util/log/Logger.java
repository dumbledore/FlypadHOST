/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.flypad.util.log;

/**
 *
 * @author albus
 */
public interface Logger {
    public void log(String message);
    public void logError(String message);
    public void log(int number);
    public void log(double number);
    public void log(boolean bool);
    public void log(Throwable t);
}
