package com.housingsimulator.exceptions;


import java.io.IOException;

/**
 * Exception indicating an error whilst processing energy suppliers data.
 */
public class NoEnergySuppliersExistException extends IOException {
    /**
     * Default constructor
     * @param message the exception message
     */
    public NoEnergySuppliersExistException(String message) {
        super(message);
    }
}
