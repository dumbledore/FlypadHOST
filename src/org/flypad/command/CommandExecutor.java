/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.flypad.command;

import org.flypad.io.bluetooth.BluetoothListener;
import org.flypad.util.DataQueue;
import org.flypad.util.SimpleThread;
import org.flypad.joystick.Joystick;
import org.flypad.joystick.JoystickConstants;
import org.flypad.util.log.ConsoleLogger;

/**
 *
 * @author albus
 */
public class CommandExecutor
        extends SimpleThread
        implements BluetoothListener, Commands, JoystickConstants {

    private static final int WHEEL_STEER = ANALOG_ROTATION_X;
    private static final int WHEEL_ANALOG_1 = ANALOG_AXIS_X;
    private static final int WHEEL_ANALOG_2 = ANALOG_AXIS_Y;
    private static final int[] WHEEL_DIGITAL = {12, 13, 14, 15};

    private DataQueue queue = new DataQueue(128);
    private ConsoleLogger logger = new ConsoleLogger();

    /*
     * Wheel config data
     */
    private final static int WHEEL_MAX_RADIUS = 40;
    private final static int WHEEL_STEP = ANALOG_MID / WHEEL_MAX_RADIUS;
    private final static int ANALOG_INTERVAL = ANALOG_MAX - ANALOG_MIN;

    public void receive(byte[] data) {
        queue.enqueue(data);
    }

    public final void run() {
        try {
            final Joystick joystick = new Joystick();

            try {
                while(isWorking()) {
                    if (!queue.isEmpty()) {
                        byte[] data = queue.dequeue();
                        
                        if (data != null) {
                            try {
                                final byte command = data[0];

                                switch (command) {
                                    case TOUCHPAD:
                                        System.out.println("TOUCHPAD!");
                                        break;

                                    case KEYBOARD:
                                        System.out.println("KEYBOARD");
                                        break;

                                    case DRIVING_WHEEL_XYZ_DATA:
                                        final double x, y, z;
                                        x = readDouble(data, 1);
                                        y = readDouble(data, 10);
                                        z = readDouble(data, 19);

                                        processWheel(joystick, x, y, z);
                                        break;

                                    case DRIVING_WHEEL_RESET:
                                        wheelReset(joystick);
                                        break;

                                    case DRIVING_WHEEL_ANALOG:
                                        final byte analogNumber = data[1];
                                        final int value = readInt(data, 2);
                                        final int maxValue = readInt(data, 6);

                                        wheelAnalog(joystick, analogNumber,
                                                value, maxValue);
                                        break;

                                    case DRIVING_WHEEL_DIGITAL:
                                        final byte digitalNumber = data[1];
                                        wheelDigital(joystick, digitalNumber);
                                        break;

                                    default:
                                        System.out.println("UNKNOWN");
                                }
                            } catch (Exception e) {
                                System.out.println("WRONG DATA");
                            }
                        }
                    } else {
                        try {
                            sleep(100);
                        } catch (InterruptedException e) {}
                    }
                }
            } finally {
                joystick.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void wheelReset(final Joystick joystick) {
        joystick.analog[WHEEL_ANALOG_1] = ANALOG_MID;
        joystick.analog[WHEEL_ANALOG_2] = ANALOG_MID;
        joystick.digital[WHEEL_DIGITAL[0]] = DIGITAL_OFF;
        joystick.digital[WHEEL_DIGITAL[1]] = DIGITAL_OFF;
        joystick.digital[WHEEL_DIGITAL[2]] = DIGITAL_OFF;
        joystick.digital[WHEEL_DIGITAL[3]] = DIGITAL_OFF;
        trySendData(joystick);
    }

    private static void wheelAnalog(
            final Joystick joystick,
            final byte analogNumber,
            final int value,
            final int maxValue) {
        
        final float part = (float) (maxValue - value) / (float) maxValue;
        final int v = ANALOG_MIN + (int) (part * ANALOG_INTERVAL);
//        System.out.println("value: " + value);
//        System.out.println("maxValue: " + maxValue);
//        System.out.println("part: " + part);
//        System.out.println("v: " + v);
        joystick.analog[(analogNumber == 1 ? WHEEL_ANALOG_1 : WHEEL_ANALOG_2)] = v;
        trySendData(joystick);
    }

    private static void wheelDigital(
            final Joystick joystick,
            final byte digitalNumber) {
        joystick.digital[WHEEL_DIGITAL[digitalNumber]] = DIGITAL_ON;
        trySendData(joystick);
    }

    private static void processWheel(
            final Joystick joystick,
            final double x,
            final double y,
            final double z) {
        
//        final int acc = aTan2(x, z);
        final int steer = aTan2(x, y) + 90;

        if (steer == 180) {
            joystick.analog[WHEEL_STEER] = ANALOG_MID;
        } else if (steer <= 180 - WHEEL_MAX_RADIUS) {
            joystick.analog[WHEEL_STEER] = ANALOG_MAX;
        } else if (steer >= 180 + WHEEL_MAX_RADIUS) {
            joystick.analog[WHEEL_STEER] = ANALOG_MIN;
        } else {
            if (steer < 180) {
                final int k = 180 - steer;
                final int value = (k * WHEEL_STEP) + ANALOG_MID;
                joystick.analog[WHEEL_STEER] =
                        (value < ANALOG_MAX ? value : ANALOG_MAX);
            } else {
                final int k = steer - 180;
                final int value = ANALOG_MID - (k * WHEEL_STEP);
                joystick.analog[WHEEL_STEER] =
                        (value > ANALOG_MIN ? value : ANALOG_MIN);
            }
        }

//        if (90 <= acc && acc <= 120) { //i.e. range + 180 flip
//            joystick.analog[ANALOG_AXIS_Y] = ANALOG_MAX;
//            joystick.analog[ANALOG_AXIS_Z] = ANALOG_MIN;
//            joystick.digital[0] = DIGITAL_ON;
//            joystick.digital[1] = DIGITAL_OFF;
//        } else if (135 <= acc && acc <= 180 || acc <= 0) { //i.e. range + 180 flip
//            joystick.analog[ANALOG_AXIS_Y] = ANALOG_MIN;
//            joystick.analog[ANALOG_AXIS_Z] = ANALOG_MAX;
//            joystick.digital[0] = DIGITAL_OFF;
//            joystick.digital[1] = DIGITAL_ON;
//        } else {
//            joystick.analog[ANALOG_AXIS_Y] = ANALOG_MIN;
//            joystick.analog[ANALOG_AXIS_Z] = ANALOG_MIN;
//            joystick.digital[0] = DIGITAL_OFF;
//            joystick.digital[1] = DIGITAL_OFF;
//        }

        trySendData(joystick);
    }

    private static void trySendData(final Joystick joystick) {
        try {
            joystick.send();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int aTan2(final double d, final double d1) {
        double d2 = 0.78539816339744828D;
        double d3 = 3D * d2;
        double d4 = Math.abs(d);
        double d5;

        if(d1 >= 0.0D) {
            double d6 = (d1 - d4) / (d1 + d4);
            d5 = d2 - d2 * d6;
        } else {
            double d7 = (d1 + d4) / (d4 - d1);
            d5 = d3 - d2 * d7;
        }
        
        return (int) Math.floor(Math.toDegrees(d >= 0.0D ? d5 : -d5));
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
        return  ( data[offset    ]         << 24) +
                ((data[offset + 1] & 0xFF) << 16) +
                ((data[offset + 2] & 0xFF) << 8) +
                ( data[offset + 3] & 0xFF);
    }

    public void connected() {
        logger.log("Connected!");
    }

    public void lostConnection() {
        logger.log("Connection lost!");
    }

    public void infoMessage(String message) {
        logger.log(message);
    }

    public void errorMessage(String message) {
        logger.logError(message);
    }
}
