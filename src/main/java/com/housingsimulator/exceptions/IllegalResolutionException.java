package com.housingsimulator.exceptions;

/**
 * Exception representing an attempt of setting a resolution to an array with size other than 2
 */
public class IllegalResolutionException extends RuntimeException {
    /**
     * Default constructor
     * @param message the exception message
     */
    public IllegalResolutionException(String message) {
        super(message);
    }
}
