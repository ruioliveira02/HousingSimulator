package com.housingsimulator.exceptions;

/**
 * An exception indicating that an illegal volume was passed to a speaker.
 * A volume is legal if it is from 0 to 100, inclusive
 */
public class IllegalVolumeException extends RuntimeException {
    /**
     * Default constructor
     * @param message the exception message
     */
    public IllegalVolumeException(String message) {
        super(message);
    }
}
