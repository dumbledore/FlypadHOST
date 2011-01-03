package org.flypad.util.log;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author albus
 */
public class ConsoleLogger extends AbstractLogger {

    public ConsoleLogger() {
        super();
    }

    public ConsoleLogger(final Logger logger) {
        super(logger);
    }

    protected final void logInternally(final String message, final boolean error) {
        if (error) {
            System.err.println(message);
        } else {
            System.out.println(message);
        }
    }

    protected final void logInternally(final Throwable t) {
        t.printStackTrace();
    }
}
