package com.housingsimulator.exceptions;


import java.io.IOException;

/**
 * Exception indicating an error whilst processing houses data.
 */
public class NoHousesExistException extends IOException {
    /**
     * Default constructor
     * @param message the exception message
     */
    public NoHousesExistException(String message) {
        super(message);
    }
}
