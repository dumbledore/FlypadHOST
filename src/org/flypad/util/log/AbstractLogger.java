/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.flypad.util.log;

/**
 *
 * @author albus
 */
public abstract class AbstractLogger implements Logger {
    private final Logger logger;

    public AbstractLogger() {
        logger = null;
    }

    public AbstractLogger(final Logger logger) {
        this.logger = logger;
    }

    public void log(final String message) {
        log(message, false);
    }

    public void logError(final String message) {
        log(message, true);
    }

    private void log(final String message, final boolean error) {
        logInternally(message, error);

        if (logger != null) {
            if (error) {
                logger.logError(message);
            } else {
                logger.log(message);
            }
        }
    }

    public void log(final int number) {
        log(Integer.toString(number));
    }

    public void log(final double number) {
        log(Double.toString(number));
    }

    public void log(final boolean bool) {
        log(bool ? "true" : "false");
    }

    public void log(final Throwable t) {
        logInternally(t);

        if (logger != null) {
            logger.log(t);
        }
    }

    protected abstract void logInternally(String message, boolean error);
    protected abstract void logInternally(Throwable t);
}
